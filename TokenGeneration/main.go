package main

import (
	"bytes"
	"cloud.google.com/go/firestore"
	"context"
	"encoding/json"
	firebase "firebase.google.com/go"
	"firebase.google.com/go/auth"
	"flag"
	"fmt"
	"google.golang.org/api/iterator"
	"google.golang.org/api/option"
	"io/ioutil"
	"log"
	"net/http"
	"strconv"
	"strings"
)

func main() {
	ctx := context.Background()
	sa := option.WithCredentialsFile("bbdd-go-firebase-adminsdk-11p69-0286d5e162.json")
	app, err := firebase.NewApp(ctx, nil, sa)
	if err != nil {
		log.Fatalln(err)
	}

	client, err := app.Auth(ctx)
	if err != nil {
		log.Fatalf("error getting Auth client: %v\n", err)
	}

	usuario, err := app.Firestore(ctx)
	if err != nil {
		log.Fatalf("error getting User client: %v\n", err)
	}

	//====================VARIABLES========================//
	var selectedOp string
	var roleName string
	var provider string
	var userName string
	var token string
	var userQuery []string
	var apikey string

	//====================FLAGS========================//
	flagRole := flag.String("role", "", "a string")
	flagUser := flag.String("user", "", "a string")
	flagAPI := flag.String("apik", "", "a string")

	flag.Parse()

	//===================ROLE AND NAME SELECTION============================//
	if *flagRole == "" || *flagUser == "" || *flagAPI == "" {

		for {
			selectedOp, _ = selectRole() //Selección rol usuario

			if selectedOp == "" {
				break
			}

			//===============USER LIST==================//
			roleName, provider, _ = distinctUser(selectedOp)
			userQuery, _ = listUsers(usuario, roleName, ctx) // Creo la lista de usuarios actuales de la colección

			fmt.Println()
			fmt.Println("User list:") //Muestro la lista índice-valor
			for i, n := range userQuery {
				fmt.Println(i, n)
			}

			userName, _ = selectUser(userQuery) //Selección de user de la lista de users userQuery

			//===============GET CLAIMS==================//

			claimsSup, uid, _ := getClaims(selectedOp, userName, usuario, roleName, ctx, provider)

			//===============GENERATE TOKEN==================//
			if apikey == "" {	//Pide clave API solo la primera vez. Para otro API se debe reinciar.
				fmt.Print("Introduce API key: ")
				_, err = fmt.Scanln(&apikey)
				for len(apikey) != 39  {
					fmt.Println("You must introduce a valid API key.")
					fmt.Print("Introduce API key: ")
					_, err = fmt.Scanln(&apikey)
				}
			}

			token, _ = GenerateToken(client, ctx, uid, claimsSup, apikey) //Generar token

			//===============DATA DISPLAY==================//
			displayFormat(selectedOp, roleName, userName, token)

		}
	}else{

		roleName, provider, _ = distinctUser(*flagRole)	//Obtener rol y provider

		claims, uid, _ := getClaims(*flagRole, *flagUser, usuario, roleName, ctx, provider)	//Crear claims de usuario

		token, _ = GenerateToken(client, ctx, uid, claims, *flagAPI) //Generar token

		displayFormat(*flagRole, roleName, *flagUser, token)
	}
}

//=======================UTILITY FUNCTIONS=========================//
func selectRole () (string, error){
	validOp := []string{"0", "1", "2", "3"}	//Opciones disponibles para selección de rol
	var selectedOp string

	fmt.Print("Select role (or leave empty to exit): Patient(0), Therapist(1), CentreAdmin(2), SuperUser(3): ")
	_, err := fmt.Scanln(&selectedOp)

	if selectedOp == "" {	//Si la opción es cadena vacía se termina el programa
		fmt.Println("Goodbye!")
		return "", nil
	}

	for err != nil || !StringInSliceOp(selectedOp, validOp) {
		selectedOp = ""
		fmt.Println("You must select a valid option.")
		fmt.Print("Select role (or leave empty to exit): Patient(0), Therapist(1), CentreAdmin(2), SuperUser(3): ")
		_, err = fmt.Scanln(&selectedOp)

		if selectedOp == "" {	//Si la opción es cadena vacía se termina el programa
			fmt.Println("Goodbye!")
			return "", nil
		}
	}

	return selectedOp, nil
}	//Obtener opción de rol

func distinctUser(selectedOp string) (string, string, error){
	var roleName string
	var provider string

	switch {
	case strings.TrimRight(selectedOp, "\n") == "0": //Paciente
		roleName = "patient"
		provider = "anonymous"
	case strings.TrimRight(selectedOp, "\n") == "1": //Terapeuta
		roleName = "therapist"
		provider = "email"
	case strings.TrimRight(selectedOp, "\n") == "2": //CentreAdmin
		roleName = "centre-admin"
		provider = "email"
	case strings.TrimRight(selectedOp, "\n") == "3": //Superuser
		roleName = "super-user"
		provider = "custom"
	}

	return roleName, provider, nil
}	//Obtener rol y provider a partir de opción de rol

func listUsers(usuario *firestore.Client, roleName string,
	ctx context.Context) ([]string, error) {
	var userQuery []string

	iter := usuario.Collection(roleName).Documents(ctx) //Creo la lista de usuarios actuales
	for {
		doc, err := iter.Next()
		if err == iterator.Done {
			return userQuery, nil
		}

		if err != nil {
			return nil, nil
		}

		userQuery = append(userQuery, doc.Data()["Name"].(string))
	}
}	//Crear lista de usuarios existentes en la colección

func selectUser(userQuery []string) (string, error){
	var iUserName string

	fmt.Print("Select user for token generation: ")
	_, err := fmt.Scanln(&iUserName)
	if err != nil{
		iUserName = "-1"
	}

	intVar, err := strconv.Atoi(iUserName)	//Conversión string to int

	for intVar >= len(userQuery) || intVar < 0 || err != nil {	//Si el valor no es válido, se sigue pidiendo
		fmt.Println("Invalid user.")
		fmt.Print("Select user for token generation: ")
		_, err = fmt.Scanln(&iUserName)
		intVar, err = strconv.Atoi(iUserName)
	}

	return userQuery[intVar], nil
}	//Seleccionar user de la query actual

func getClaims(selectedOp string, userName string, usuario *firestore.Client, roleName string,
	ctx context.Context, provider string) (map[string]interface{}, string, error){
	var claims map[string]interface{}
	var uid string
	var uu string
	var centreUid string

	if selectedOp == "0" || selectedOp == "3" { //Paciente y superUser
		uu, uid, _ = GetUser(userName, usuario, roleName, ctx) //Busco el usuario en la base de datos

		claims = map[string]interface{}{ //Claims Paciente y superUser
			"role":        roleName,
			"user_id":     uu,
			"provider_id": provider,
		}

	} else { //Terapeuta y CentreAdmin
		uu, uid, centreUid, _ = GetUserWithCentre(userName, usuario, ctx) //Busco el usuario en la base de datos

		claims = map[string]interface{}{ //Claims Terapeuta y CentreAdmin
			"role":        roleName,
			"user_id":     uu,
			"centre_id":   centreUid,
			"provider_id": provider,
		}
	}
	claimsSup := map[string]interface{}{ //Bajar claims de nivel
		"claims": claims,
	}

	return claimsSup, uid, nil

}	//Crear claims usuario

func GetUserWithCentre (userName string, usuario *firestore.Client,
	ctx context.Context ) (string, string, string, error) {	//Therapist and CentreAdmin
	
	iter := usuario.Collection("UUID").Where("Name", "==", userName).Documents(ctx)	//Busca coincidencia
	doc, err := iter.Next()
	
	if err != nil{
		return "", "", "", err
	}

	uu := doc.Data()["UUID"].(string)
	uid := doc.Data()["UID"].(string)
	centreUid := doc.Data()["CentreUid"].(string)
	return uu, uid, centreUid, nil
}	//Obtener user con centro de la colección

func GetUser (userName string, usuario *firestore.Client, roleName string,
	ctx context.Context ) (string, string, error){ 	//Patient and SuperUser
	iter := usuario.Collection(roleName).Where("Name", "==", userName).Documents(ctx)
	doc, err := iter.Next()
	
	if err != nil {
		return "", "", err
	}
	
	uu := doc.Data()["UUID"].(string)
	uid := doc.Data()["UID"].(string)
	return uu, uid, nil
}	//Obtener user sin centro de la colección

func GenerateToken(client *auth.Client, ctx context.Context, uid string,
	claims map[string]interface{}, apikey string) (string, error){	//Generación de token

	token, _ := client.CustomTokenWithClaims(ctx, uid, claims)	//Se genera token

	postBody, _ := json.Marshal(map[string]interface{}{	//Creación cuerpo petición
		"token": token,
		"returnSecureToken": true,
	})
	responseBody := bytes.NewBuffer(postBody)	//Buffer

	resp, err := http.Post("https://identitytoolkit.googleapis.com/v1/accounts:signInWithCustomToken?key=" + apikey,
		"application/json", responseBody)	//Respuesta de la petición

	if err != nil {
		log.Fatalf("An error ocurred %v\n", err)
	}

	body, err := ioutil.ReadAll(resp.Body)	//Lectura respuesta petición

	if err != nil {
		log.Fatalln(err)
	}
	sb := string(body)	//Pasar respuesta a string

	tk := strings.Split(sb, ",\n")	//Obtener solo Token_id
	tk2 := strings.Split(tk[1], " ")

	return tk2[3], nil
}	//Petición y respuesta

func StringInSliceOp(s string, list []string) bool{ //Comprueba si la opción del rol está en la lista
	for _, b := range list{
		if b == s {
			return true
		}
	}
	return false
}	//Comprobación de correcta selección de opciones

func displayFormat (selectedOp string, roleName string, userName string,
	token string) {
	validOp := []string{"0", "1"} //Opciones disponibles de data display

	fmt.Println()
	fmt.Print("Select data display format: Token(0), Payload(1): ")
	_, err := fmt.Scanln(&selectedOp)

	for err != nil || !StringInSliceOp(selectedOp, validOp) { //Si el error persiste seguir pidiendo muestra de información
		fmt.Println("You must select a data display format")
		fmt.Print("Select data display format: Token(0), Payload(1): ")
		_, err = fmt.Scanln(&selectedOp)
	} //Una vez seleccionado muestra la información

	if selectedOp == "0" { //Muestra el token string entero
		fmt.Println("--Data display--")
		fmt.Printf("Selected role: %v\n", roleName)
		fmt.Printf("User name: %v\n", userName)
		fmt.Println()
		fmt.Printf("Got token: %v\n", token)

	} else { //Solo muestra el string del token content
		tokenSlices := strings.Split(token, ".")
		fmt.Println("--Data display--")
		fmt.Printf("Selected role: %v\n", roleName)
		fmt.Printf("User name: %v\n", userName)
		fmt.Println()
		fmt.Printf("Payload token slice: %v\n", tokenSlices[1])
	}
	fmt.Println()
} //Selección de muestra del token