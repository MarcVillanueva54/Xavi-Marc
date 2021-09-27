package main

import "time"

type Patient struct {
	id 			string
	fileId		string
	centreId 	string
	therapistId	string
	studyId		string
	active		bool
	createAt	time.Time
}

//func generateToken (op string)