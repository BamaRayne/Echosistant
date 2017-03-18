/* 
 * Notification - EchoSistant Add-on 
 *
 *		3/16/2017		Version:4.0 R.0.3.0	    Cron Scheduling and Reporting
 *
 *  Copyright 2016 Jason Headley & Bobby Dobrescu
 *
 *  Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 *  in compliance with the License. You may obtain a copy of the License at:
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software distributed under the License is distributed
 *  on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License
 *  for the specific language governing permissions and limitations under the License.
 *
/**********************************************************************************************************************************************/
definition(
	name			: "NotificationProfile",
    namespace		: "Echo",
    author			: "JH/BD",
	description		: "EchoSistant Add-on",
	category		: "My Apps",
    parent			: "Echo:EchoSistant", 
	iconUrl			: "https://raw.githubusercontent.com/BamaRayne/Echosistant/master/smartapps/bamarayne/echosistant.src/app-Echosistant.png",
	iconX2Url		: "https://raw.githubusercontent.com/BamaRayne/Echosistant/master/smartapps/bamarayne/echosistant.src/app-Echosistant@2x.png",
	iconX3Url		: "https://raw.githubusercontent.com/BamaRayne/Echosistant/master/smartapps/bamarayne/echosistant.src/app-Echosistant@2x.png")
/**********************************************************************************************************************************************/
private release() {
	def text = "R.0.3.0"
}

preferences {

    page name: "mainProfilePage"
    		page name: "pNotifyScene"          
        	page name: "pNotifications"
        	page name: "pRestrict"
            page name: "pNotifyConfig"
            page name: "SMS"
            page name: "customSounds"
            page( name: "timeIntervalInput", title: "Only during a certain time")

}
//dynamic page methods
page name: "mainProfilePage"
    def mainProfilePage() {
 	dynamicPage (name: "mainProfilePage", install: true, uninstall: true) {
		section ("Name (rename) this Profile") {
 		   	label title:"Profile Name ", required:false, defaultValue: "Notification Profile"  
		}
		section ("Create a Notification") {
                
                input "actionType", "enum", title: "Choose the message output...", required: false, defaultValue: "Default", submitOnChange: true, options: [
                "Ad-Hoc Report",
                "Triggered Report",
                "Custom",
                "Custom with Weather",
                "Default",
				"Bell 1",
				"Bell 2",
				"Dogs Barking",
				"Fire Alarm",
				"The mail has arrived",
				"A door opened",
				"There is motion",
				"Smartthings detected a flood",
				"Smartthings detected smoke",
				"Someone is arriving",
				"Piano",
				"Lightsaber"]
		}
        if (actionType == "Custom" || actionType == "Custom with Weather" || actionType == "Ad-Hoc Report") {
            section ("Send this message text...") {
                input "message", "text", title: "Play this message...", required:false, multiple: false, defaultValue: ""
            }
            section ("Tap here to see available variables", hideable: true, hidden: true) {    
                if (actionType != "Ad-Hoc Report") paragraph 	"You can use the following variables in your custom message: "+
                												"&device, &action , &event, &time &date and &profile \n" +
                    											"\nFor Example: \n&event sensor &device is &action and the event happened at &time \n" +
                    											"Translates to: 'Contact' sensor 'Bedroom' is 'Open' and the event happened at '1:00 PM'"
				if(actionType == "Custom with Weather" || actionType == "Ad-Hoc Report" ){
                	paragraph "WEATHER VARIABLES: &today, &tonight, &tomorrow, &high, &low, &wind, &uv, &precipitation, &humidity, &conditions \n"                    
                }
                if(actionType == "Ad-Hoc Report"){
                	paragraph "REPORTING VARIABLES: &time, &date, &profile, &mode, &shm, &power, &lights, &doors, &windows, &open, &garage, &unlocked, &temperature, &running, &thermostat, &present"
                }
            }
        } 
		def sTitle = actionType != "Ad-Hoc Report" ? "Trigger(s)" : "Device(s)"
        section ("Using These ${sTitle}") {
			href "triggers", title: "Select ${sTitle}", description: triggersComplete(), state: triggersSettings()
        }    
        if (actionType != "Ad-Hoc Report"){
            section ("With these output methods" , hideWhenEmpty: true) {    
                input "sonos", "capability.musicPlayer", title: "On this Music Player", required: false, multiple: true, submitOnChange: true
                    if (sonos) {
                        input "sonosVolume", "number", title: "Temporarily change volume", description: "0-100%", required: false
                    	input "resumePlaying", "bool", title: "Resume currently playing music after notification", required: false, defaultValue: false
                    }
                input "speechSynth", "capability.speechSynthesis", title: "On this Speech Synthesis Device", required: false, multiple: true, submitOnChange: true
                        if (speechSynth) {
                            input "speechVolume", "number", title: "Temporarily change volume", description: "0-100%", required: false
                    }
                href "SMS", title: "Send SMS & Push Messages...", description: pSendComplete(), state: pSendSettings()
            }
            section ("Run actions for this Profile" ) {    
                input "myProfile", "enum", title: "Choose Profile...", options: getProfileList(), multiple: false, required: false 
            }        
            section ("Using these Restrictions") {
                href "pRestrict", title: "Use these restrictions...", description: pRestComplete(), state: pRestSettings()
            }
		}
 	}
}
page name: "triggers"
	def triggers(){
		dynamicPage(name: "triggers", title: "", uninstall: false) {
            def actions = location.helloHome?.getPhrases()*.label.sort()
            if(actionType != "Default" && actionType != "Ad-Hoc Report" ){
                section("Time") {
                    input "frequency", "enum", title: "Choose a Frequency", submitOnChange: true, required: fale, 
            			options: ["Minutes", "Hourly", "Daily", "Weekly", "Monthly", "Yearly"]
                	if(frequency == "Minutes"){
                        input "xMinutes", "number", title: "Every X minute(s) - maximum 60", range: "1..59", submitOnChange: true, required: false
                	}
                    if(frequency == "Hourly"){
                        input "xHours", "number", title: "Every X hour(s) - maximum 24", range: "1..23", submitOnChange: true, required: false
                    }	
                    if(frequency == "Daily"){
                        input "xDays", "number", title: "Every X day(s) - maximum 31", range: "1..31", submitOnChange: true, required: false
						input "xDaysWeekDay", "bool", title: "OR Every Week Day (MON-FRI)", required: false, defaultValue: false
                        if(xDays || xDaysWeekDay){input "xDaysStarting", "time", title: "starting at time...", submitOnChange: true, required: true}
                    }
                    if(frequency == "Weekly"){
						input "xWeeks", "enum", title: "Every selected day(s) of the week", submitOnChange: true, required: false, multiple: true,
							options: ["SUN": "Sunday", "MON": "Monday", "TUE": "Tuesday", "WED": "Wednesday", "THU": "Thursday", "FRI": "Friday", "SAT": "Saturday"]                        
                        if(xWeeks){input "xWeeksStarting", "time", title: "starting at time...", submitOnChange: true, required: true}
                    }
                    if(frequency == "Monthly"){
                    	//TO DO add every (First-Fourth), (Mon-Fri) of every (X) month
                        input "xMonths", "number", title: "Every X month(s) - maximum 12", range: "1..12", submitOnChange: true, required: false
                        if(xMonths){
                            input "xMonthsDay", "number", title: "...on this day of the month", range: "1..31", submitOnChange: true, required: true
                            input "xMonthsStarting", "time", title: "starting at time...", submitOnChange: true, required: true
                        }
                    }
                    if(frequency == "Yearly"){
                    	//TO DO add the (First-Fourth), (Mon-Fri) of (Jan-Dec)
                        input "xYears", "enum", title: "Every selected month of the year", submitOnChange: true, required: false, multiple: false,
                        	options: ["1": "January", "2":"February", "3":"March", "4":"April", "5":"May", "6":"June", "7":"July", "8":"August", "9":"September", "10":"October", "11":"November", "12":"December"]
                        if(xYears){
                            input "xYearsDay", "number", title: "...on this day of the month", range: "1..31", submitOnChange: true, required: true
                            input "xYearsStarting", "time", title: "starting at time...", submitOnChange: true, required: true                     
						}
                	}
                }
			}                
            if(actionType != "Default"){
                section ("Location Event", hideWhenEmpty: true) {
                    input "myMode", "enum", title: "Choose Modes...", options: location.modes.name.sort(), multiple: true, required: false 
                    if (actionType != "Ad-Hoc Report") input "myRoutine", "enum", title: "Choose Routines...", options: actions, multiple: true, required: false            
                }
			}            
            section ("Device State", , hideWhenEmpty: true) {
                input "mySwitch", "capability.switch", title: "Choose Switch(es)...", required: false, multiple: true, submitOnChange: true
                    if (mySwitch && actionType != "Ad-Hoc Report") input "mySwitchS", "enum", title: "Notify when state changes to...", options: ["on", "off", "both"], required: false
                if(actionType != "Default") {
                input "myPower", "capability.powerMeter", title: "Choose Power Meters...", required: false, multiple: false, submitOnChange: true
                    if (myPower && actionType != "Ad-Hoc Report") input "myPowerS", "enum", title: "Notify when power is...", options: ["above threshold", "below threshold"], required: false, submitOnChange: true
                        if (myPowerS) input "threshold", "number", title: "Wattage Threshold...", required: false, description: "in watts", submitOnChange: true
                        if (threshold) input "minutes", "number", title: "Threshold Delay", required: false, description: "in minutes (optional)"
                        if (threshold) input "thresholdStop", "number", title: "...but not above/below this value", required: false, description: "in watts"
                }
                input "myLocks", "capability.lock", title: "Choose Locks..", required: false, multiple: true, submitOnChange: true
                    if (myLocks && actionType != "Ad-Hoc Report") input "myLocksS", "enum", title: "Notify when state changes to...", options: ["locked", "unlocked", "both"], required: false
                if(actionType != "Default"){
                input "myTstat", "capability.thermostat", title: "Choose Thermostats...", required: false, multiple: true, submitOnChange: true
                    if (myTstat && actionType != "Ad-Hoc Report") input "myTstatS", "enum", title: "Notify when set point changes for...", options: ["cooling", "heating", "both"], required: false
                    // attribute thermostatMode
                    if (myTstat && actionType != "Ad-Hoc Report") input "myTstatM", "enum", title: "Notify when mode changes to...", options: ["auto", "cool", " heat", "emergency heat", "off", "every mode"], required: false
                    // attribute thermostatOperatingState
                    if (myTstat && actionType != "Ad-Hoc Report") input "myTstatOS", "enum", title: "Notify when Operating State changes to...", options: ["cooling", "heating", " idle", "every state"], required: false
            	}
            }
            section ("Sensor Status", hideWhenEmpty: true) {
                input "myContact", "capability.contactSensor", title: "Choose Doors and Windows..", required: false, multiple: true, submitOnChange: true
                    if (myContact && actionType != "Ad-Hoc Report") input "myContactS", "enum", title: "Notify when state changes to...", options: ["open", "close", "both"], required: false
                input "myMotion", "capability.motionSensor", title: "Choose Motion Sensors..", required: false, multiple: true, submitOnChange: true
                    if (myMotion && actionType != "Ad-Hoc Report") input "myMotionS", "enum", title: "Notify when state changes to...", options: ["active", "inactive", "both"], required: false
                input "myPresence", "capability.presenceSensor", title: "Choose Presence Sensors...", required: false, multiple: true, submitOnChange: true
                    if (myPresence && actionType != "Ad-Hoc Report") input "myPresenceS", "enum", title: "Notify when state changes to...", options: ["present", "not present", "both"], required: false
                input "mySmoke", "capability.smokeDetector", title: "Choose Smoke Detectors...", required: false, multiple: true, submitOnChange: true
                    if (mySmoke && actionType != "Ad-Hoc Report") input "mySmokeS", "enum", title: "Notify when state changes to...", options: ["detected", "clear", "both"], required: false
                input "myWater", "capability.waterSensor", title: "Choose Water Sensors...", required: false, multiple: true, submitOnChange: true
                    if (myWater && actionType != "Ad-Hoc Report") input "myWaterS", "enum", title: "Notify when state changes to...", options: ["wet", "dry", "both"], required: false		
            }
            if(actionType != "Default" && actionType != "Ad-Hoc Report"){
                section ("Weather Events") {
                    input "myWeatherAlert", "enum", title: "Choose Weather Alerts...", required: false, multiple: true, submitOnChange: true,
                            options: [
                            "TOR":	"Tornado Warning",
                            "TOW":	"Tornado Watch",
                            "WRN":	"Severe Thunderstorm Warning",
                            "SEW":	"Severe Thunderstorm Watch",
                            "WIN":	"Winter Weather Advisory",
                            "FLO":	"Flood Warning",
                            "WND":	"High Wind Advisoryt",
                            "HEA":	"Heat Advisory",
                            "FOG":	"Dense Fog Advisory",
                            "FIR":	"Fire Weather Advisory",
                            "VOL":	"Volcanic Activity Statement",
                            "HWW":	"Hurricane Wind Warning"
                            ]          
                    input "myWeather", "enum", title: "Choose Hourly Weather Forecast Updates...", required: false, multiple: false, submitOnChange: true,
                            options: ["Weather Condition Changes", "Chance of Precipitation Changes", "Wind Speed Changes", "Humidity Changes", "Any Weather Updates"]   
                } 
            }        
		}
	}
page name: "SMS"
    def SMS(){
        dynamicPage(name: "SMS", title: "Send SMS and/or Push Messages...", uninstall: false) {
        section ("Push Messages") {
            input "push", "bool", title: "Send Push Notification...", required: false, defaultValue: false
            input "timeStamp", "bool", title: "Add time stamp to Push Messages...", required: false, defaultValue: false  
            }
        section ("Text Messages" , hideWhenEmpty: true) {
            input "sendContactText", "bool", title: "Enable Text Notifications to Contact Book (if available)", required: false, submitOnChange: true
                if (sendContactText){
                    input "recipients", "contact", title: "Send text notifications to...", multiple: true, required: false
                }
            input "sendText", "bool", title: "Enable Text Notifications to non-contact book phone(s)", required: false, submitOnChange: true      
                if (sendText){      
                    paragraph "You may enter multiple phone numbers separated by comma to deliver the Alexa message as a text and a push notification. E.g. 8045551122,8046663344"
                    input name: "sms", title: "Send text notification to...", type: "phone", required: false
                }
            }    
        }        
    }
page name: "pRestrict"
    def pRestrict(){
        dynamicPage(name: "pRestrict", title: "", uninstall: false) {
			section ("Mode Restrictions") {
                input "modes", "mode", title: "Only when mode is", multiple: true, required: false, submitOnChange: true
            }        
            section ("Days - Audio only on these days"){	
                input "days", title: "Only on certain days of the week", multiple: true, required: false, submitOnChange: true,
                    "enum", options: ["Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday", "Sunday"]
            }
            section ("Time - Audio only during these times"){
                href "certainTime", title: "Only during a certain time", description: pTimeComplete(), state: pTimeSettings()
            }   
	    }
	}
page name: "certainTime"
    def certainTime() {
        dynamicPage(name:"certainTime",title: "Only during a certain time", uninstall: false) {
            section("Beginning at....") {
                input "startingX", "enum", title: "Starting at...", options: ["A specific time", "Sunrise", "Sunset"], required: false , submitOnChange: true
                if(startingX in [null, "A specific time"]) input "starting", "time", title: "Start time", required: false, submitOnChange: true
                else {
                    if(startingX == "Sunrise") input "startSunriseOffset", "number", range: "*..*", title: "Offset in minutes (+/-)", required: false, submitOnChange: true
                    else if(startingX == "Sunset") input "startSunsetOffset", "number", range: "*..*", title: "Offset in minutes (+/-)", required: false, submitOnChange: true
                }
            }
            section("Ending at....") {
                input "endingX", "enum", title: "Ending at...", options: ["A specific time", "Sunrise", "Sunset"], required: false, submitOnChange: true
                if(endingX in [null, "A specific time"]) input "ending", "time", title: "End time", required: false, submitOnChange: true
                else {
                    if(endingX == "Sunrise") input "endSunriseOffset", "number", range: "*..*", title: "Offset in minutes (+/-)", required: false, submitOnChange: true
                    else if(endingX == "Sunset") input "endSunsetOffset", "number", range: "*..*", title: "Offset in minutes (+/-)", required: false, submitOnChange: true
                }
            }
        }
    }
/************************************************************************************************************
		
************************************************************************************************************/
def installed() {
	log.debug "Installed with settings: ${settings}, current app version: ${release()}"
    state.NotificationRelease = "Notification: " + release()

	if (myWeatherAlert) {
		runEvery5Minutes(mGetWeatherAlerts)
	}
	if (myWeather) {
		runEvery1Hour(mGetCurrentWeather)
	}    
}
def updated() {
	log.debug "Updated with settings: ${settings}, current app version: ${release()}"
	state.NotificationRelease = "Notification: " + release()
    state.lastPlayed = now()
	unschedule()
    unsubscribe()
    initialize()
}
def initialize() {
	state.lastTime
    state.lastWeatherCheck
    state.lastAlert
    state.cycleOnH = false
    state.cycleOnL = false
    state.lastWeather
    if (frequency) cronHandler(frequency)
    if (myWeatherAlert) {
		runEvery5Minutes(mGetWeatherAlerts)
		state.weatherAlert
    }
	if (myWeather) {
    	log.debug "refreshing hourly weather"
		runEvery1Hour(mGetCurrentWeather)
        state.lastWeather = null
        state.lastWeatherCheck = null
       	mGetCurrentWeather()
	}    
    if (actionType && actionType != "Ad-Hoc Report") {
        if(myPower) 							subscribe(myPower, "power", meterHandler)
        if (myRoutine) 							subscribe(location, "routineExecuted",alertsHandler)
        if (myMode) 							subscribe(location, "mode", alertsHandler)
        if (mySwitch) {
            if (mySwitchS == "on")				subscribe(mySwitch, "switch.on", alertsHandler)
            if (mySwitchS == "off")				subscribe(mySwitch, "switch.off", alertsHandler)
            if (mySwitchS == "both")			subscribe(mySwitch, "switch", alertsHandler)
        }    
        if (myContact) {
            if (myContactS == "open")			subscribe(myContact, "contact.open", alertsHandler)
            if (myContactS == "closed")			subscribe(myContact, "contact.closed", alertsHandler)
            if (myContactS == "both")			subscribe(myContact, "contact", alertsHandler)
        }
        if (myMotion) {
            if (myMotionS == "active")			subscribe(myMotion, "motion.active", alertsHandler)
            if (myMotionS == "inactive")		subscribe(myMotion, "motion.inactive", alertsHandler)
            if (myMotionS == "both")			subscribe(myMotion, "motion", alertsHandler)
        }    
        if (myLocks) {
            if (myLocksS == "locked")			subscribe(myLocks, "lock.locked", alertsHandler)
            if (myLocksS == "unlocked")			subscribe(myLocks, "lock.unlocked", alertsHandler)
            if (myLocksS == "both")				subscribe(myLocks, "lock", alertsHandler)
        }
        if (myPresence) {
            if (myPresenceS == "present")		subscribe(myPresence, "presence.present", alertsHandler)
            if (myPresenceS == "not present")	subscribe(myPresence, "presence.not present", alertsHandler)
            if (myPresenceS == "both")			subscribe(myPresence, "presence", alertsHandler)
        }
        if (myTstat) {    
            if (myTstatS == "cooling")			subscribe(myTstat, "coolingSetpoint", alertsHandler)
            if (myTstatS == "heating")			subscribe(myTstat, "heatingSetpoint", alertsHandler)
            if (myTstatS == "both")				subscribe(myPresence, "thermostatSetpoint", alertsHandler)
        
            if (myTstatM == "auto")				subscribe(myTstat, "thermostatMode.auto", alertsHandler)
            if (myTstatM == "cool")				subscribe(myTstat, "thermostatMode.auto.cool", alertsHandler)
            if (myTstatM == "heat")				subscribe(myPresence, "thermostatMode.heat", alertsHandler)        
            if (myTstatM == "off")				subscribe(myTstat, "thermostatMode.off", alertsHandler)
            if (myTstatM == "every mode")		subscribe(myPresence, "thermostatMode", alertsHandler)
            
            
            if (myTstatOS == "cooling")			subscribe(myTstat, "thermostatOperatingState.cooling", alertsHandler)
            if (myTstatOS == "heating")			subscribe(myTstat, "thermostatOperatingState.heating", alertsHandler)
            if (myTstatOS == "idle")			subscribe(myTstat, "thermostatOperatingState.idle", alertsHandler)
            if (myTstatOS == "every state")		subscribe(myTstat, "thermostatOperatingState", alertsHandler)
		}
        if (mySmoke) {    
            if (mySmokeS == "detected")			subscribe(mySmoke, "smoke.detected", alertsHandler)
            if (mySmokeS == "clear")			subscribe(mySmoke, "smoke.clear", alertsHandler)
            if (mySmokeS == "both")				subscribe(mySmoke, "smoke", alertsHandler)
        }
        if (myWater) {    
            if (myWaterS == "wet")				subscribe(myWater, "water.wet", alertsHandler)
            if (myWaterS == "dry")				subscribe(myWater, "water.dry", alertsHandler)
            if (myWaterS == "both")				subscribe(myWater, "water", alertsHandler)
      	}
    }
}
/******************************************************************************************************
   PARENT STATUS CHECKS
******************************************************************************************************/
def checkRelease() {
return state.NotificationRelease
}
/************************************************************************************************************
   RUNNING REPORT FROM PARENT
************************************************************************************************************/
def runProfile(profile) {
	log.warn "received command from the Parent for $profile"
	def result 
	if (actionType == "Ad-Hoc Report" &&  message){
    	// date, time and profile variables
        result = message ? "$message".replace("&date", "${getVar("date")}").replace("&time", "${getVar("time")}").replace("&profile", "${getVar("profile")}") : null
        // power variables
        result = result ? "$result".replace("&power", "${getVar("power")}").replace("&lights", "${getVar("lights")}") : null
        // garage doors, locks and precence variables
        result = result ? "$result".replace("&garage", "${getVar("garage")}").replace("&unlocked", "${getVar("unlocked")}").replace("&present", "${getVar("present")}") : null
		// doors and windows variables
        result = result ? "$result".replace("&doors", "${getVar("doors")}").replace("&windows", "${getVar("windows")}").replace("&open", "${getVar("open")}") : null
		// location variables
        result = result ? "$result".replace("&mode", "${getVar("mode")}").replace("&shm", "${getVar("shm")}") : null
		//thermostat variables
        result = result ? "$result".replace("&thermostat", "${getVar("thermostat")}").replace("&running", "${getVar("running")}").replace("&temperature", "${getVar("temperature")}")  : null
		//weather variables
        result = getWeatherVar(result) 
    }
    else result = "Sorry you can only generate an ad-hoc report that has a custom message"
 	log.warn "sending Report to Main App: $result"
    return result
}
/************************************************************************************************************
   REPORT VARIABLES
   
   &time, &date, &profile, &mode, &shm, &power, &lights, &doors, &windows, &open, &garage, &unlocked, &temperature, &running, &present
   
************************************************************************************************************/
private getVar(var) {
	def devList = []
    def result
    if (var == "time"){
        result = new Date(now()).format("h:mm aa", location.timeZone) 
    	return result
    }
    if (var == "date"){
        result = new Date(now()).format("EEEE, MMMM d, yyyy", location.timeZone)
    	return result    
    }
    if (var == "profile"){
        result = app.label
    	return result 	
    }
    if (var == "mode"){
        result = location?.currentMode
    	return result 	    
    }
	if (var == "shm"){ 
		def sSHM = location?.currentState("alarmSystemStatus")?.value       
		sSHM = sSHM == "off" ? "disabled" : sSHM == "away" ? "Armed Away" : sSHM == "stay" ? "Armed Home" : "unknown"
		result = sSHM
        return result
    }    
    if (var == "power"){
        if (myPower){
            def meterValueRaw = myPower?.currentValue("power") as double
            int meter = meterValueRaw ?: 0 as int        
            result = meter
            return result
        }
    }
    if (var == "open"){
    	if (myContact){
            if (myContact?.latestValue("contact")?.contains("open")) {
                myContact?.each { deviceName ->
                    if (deviceName.latestValue("contact")=="open") {
                        String device  = (String) deviceName
                        devList += device
                    }
                }
            }
            if (devList?.size() == 1)  result = devList?.size() + " sensor"
            else if (devList?.size() > 0) result = devList?.size() + " sensors"
            else if (!devList) result = "no sensors"
            return result
    	}	
    }
    if (var == "doors"){
		if(parent.cDoor1){
			if (parent.cDoor1?.currentValue("contact").contains("open")) {
            	parent.cDoor1?.each { deviceName ->
                	if (deviceName.currentValue("contact")=="${"open"}") {
                    	String device  = (String) deviceName
                        devList += device
                    }
                }
            }
            if (devList?.size() == 1)  result = devList?.size() + " door"
            else if (devList?.size() > 0) result = devList?.size() + " doors"
            else if (!devList) result = "no doors"
            return result
    	}	
    }
    if (var == "windows"){                    
		if(parent.cWindow) {
			if (parent.cWindow?.currentValue("contact").contains("open")) {
            	parent.cWindow?.each { deviceName ->
                	if (deviceName.currentValue("contact")=="${"open"}") {
                    	String device  = (String) deviceName
                        devList += device
                    }
                }
            }
            if (devList?.size() == 1)  result = devList?.size() + " window"
            else if (devList?.size() > 0) result = devList?.size() + " windows"
            else if (!devList) result = "no windows"
            return result
    	}	
    }
    if (var == "unlocked"){                    
		if(myLocks) {
			if (myLocks.currentValue("lock").contains("unlocked")) {
            	myLocks.each { deviceName ->
                	if (deviceName.currentValue("lock")=="${"unlocked"}") {
                    	String device  = (String) deviceName
                        devList += device
                    }
                }
            }
            if (devList?.size() == 1)  result = devList?.size() + " door"
            else if (devList?.size() > 0) result = devList?.size() + " doors"
            else if (!devList) result = "no doors"
            return result
    	}	
    }
    if (var == "present"){                    
		if(myPresence) {
			if (myPresence.currentValue("presence").contains("present")) {
            	myPresence.each { deviceName ->
                	if (deviceName.currentValue("presence")=="${"present"}") {
                    	String device  = (String) deviceName
                        devList += device
                    }
                }
            }
            if (devList?.size() == 1)  result = devList?.size() + " person"
            else if (devList?.size() > 0) result = devList?.size() + " people"
            else if (!devList) result = "no people"
            return result
    	}	
    }    
    if (var == "lights"){
        if(mySwitch){
				mySwitch.each { deviceName ->
                    if (deviceName.latestValue("switch")=="on") {
                    	String device  = (String) deviceName
                        devList += device
                    }
				}
            if (devList?.size() == 1) result = devList?.size() + " switch"
            else if (devList?.size() > 0) result = devList?.size() + " switches"  
            else if (!devList) result = "no switches"
            return result
  		}
    }
    if (var == "thermostat"){    
        if(myTstat){
        def currentMode
				myTstat.each { deviceName ->
                	String device  = (String) deviceName
                    currentMode = deviceName.currentValue("thermostatMode") 
                        devList += device + " is set to "+ currentMode  
               	}
                if (!devList) result = "unknown"
                else if (devList) result = devList
                return result
   		}
   	}
    if (var == "running"){    
        if(myTstat){
        def currentOS
				myTstat.each { deviceName ->
                	String device  = (String) deviceName
                    currentOS = deviceName.currentValue("thermostatOperatingState")
                        devList += currentOS
               	}
                def tSize = devList?.size()
                log.warn "size = $tSize, $currentOS, $devList "
                if (devList && !devList.contains("idle")) result = "running"
                else if (devList && (devList.contains("cooling") || devList.contains("heating")))  result = "running"
                else if (devList && devList.contains("idle") && !devList.contains("cooling") && !devList.contains("heating")) result = "not running"
                else if (devList && devList.contains("fan only"))  result = "running the fan only"
                else result = "unknown"
                return result
   		}
   	}    
    if (var == "temperature"){    
    	if(myTstat){
           	def total = 0
    		myTstat?.each {total += it.currentValue("temperature")}
           int avgT = total as Integer
        result = Math.round(total/myTstat?.size())
        return result
		}
    }
    if (var == "garage"){    
    	if(parent.cDoor){
            result = parent.cDoor.latestValue("contact").contains("open") ? "open" : "closed"  
    	}
        else if (parent.cRelay && parent.cContactRelay)
        	result = parent.cContactRelay.latestValue("contact").contains("open") ? "open" : "closed"  
        return result
	}
}
/************************************************************************************************************
   TIME OF DAY HANDLER
************************************************************************************************************/
def scheduledTimeHandler() {
	def data = [:]
		if (getDayOk()==true && getModeOk()==true && getTimeOk()==true) {	
			data = [value:"time", name:"time of day", device:"schedule"] 
    		alertsHandler(data)
    	}
}
/***********************************************************************************************************************
    POWER HANDLER
***********************************************************************************************************************/
def meterHandler(evt) {
        def data = [:]
        def eVal = evt.value
        def eName = evt.name
        def eDev = evt.device
        def eDisplayN = evt.displayName
        int delay = minutes 
            delay = delay ?: 0 as int
        int meterValueRaw = evt.value as double
            int meterValue = meterValueRaw ?: 0 as int
        int thresholdValue = threshold == null ? 0 : threshold as int
        int thresholdStopValue = thresholdStop == null ? 0 : thresholdStop as int
        def cycleOnHigh = state.cycleOnH
        def cycleOnLow = state.cycleOnL
        if(myPowerS == "above threshold"){
            thresholdStopValue = thresholdStopValue == 0 ? 9999 :  thresholdStopValue as int
            if (meterValue > thresholdValue && meterValue < thresholdStopValue ) {
                if (cycleOnHigh == false){
                    state.cycleOnH = true
                    log.debug "Power meter $meterValue is above threshold $thresholdValue with threshold stop $thresholdStopValue"
                    if (delay) {
                        log.warn "scheduling delay ${delay}, ${60*delay}"
                        runIn(60*delay , bufferPendingH)
                    }
                    else {
                        log.debug "sending notification (above)" 
                        data = [value:"above threshold", name:"power", displayName:eDisplayN] 
                        alertsHandler(data)
                    }
                }
            }
            else {
                state.cycleOnH = false
                unschedule("bufferPendingH")
                log.debug "Power exception (above) meterValue ${meterValue}, thresholdValue ${thresholdValue}, stop ${thresholdStopValue} "
            }
        }
        if(myPowerS == "below threshold"){
            if (meterValue < thresholdValue && meterValue > thresholdStopValue) {
                if (cycleOnLow == false){
                    state.cycleOnL = true
                    log.debug "Power meter $meterValue is below threshold $thresholdValue with threshold stop $thresholdStopValue"
                    if (delay) {
                        log.warn "scheduling delay ${delay}, ${60*delay}"
                        runIn(60*delay, bufferPendingL)
                    }
                    else {
                        log.debug "sending notification (below)" 
                        data = [value:"below threshold", name:"power", displayName:eDisplayN]
                        alertsHandler(data)
                    }
                }
            }
            else {
                state.cycleOnL = false
                unschedule("bufferPendingL")
                log.debug "Power exception (below) meterValue ${meterValue}, thresholdValue ${thresholdValue}, stop ${thresholdStopValue}"
            }
        }
	}
def bufferPendingH() {  
	def meterValueRaw = myPower.currentValue("power") as double
    	int meterValue = meterValueRaw ?: 0 as int
    def thresholdValue = threshold == null ? 0 : threshold as int
    if (meterValue >= thresholdValue) {
		log.debug "sending notification (above)" 
        def data = [value:"above threshold", name:"power", displayName:eDisplayN] 
    	alertsHandler(data)
   }
}
private bufferPendingL() {  
    def meterValueRaw = myPower.currentValue("power") as double 
		int meterValue = meterValueRaw ?: 0 as int    
    def thresholdValue = threshold == null ? 0 : threshold as int
    if (meterValue <= thresholdValue) {
		log.debug "sending notification (below)" 
       def data = [value:"below threshold", name:"power", displayName:eDisplayN] 
    	alertsHandler(data)
  	}
}
/************************************************************************************************************
   EVENTS HANDLER
************************************************************************************************************/
def alertsHandler(evt) {
    def event = evt.data
    def eVal = evt.value
    def eName = evt.name
    def eDev = evt.device
    def eDisplayN = evt.displayName
    def eDisplayT = evt.descriptionText
	def eTxt = eDisplayN + " is " + eVal //evt.descriptionText 
    if(actionType == "Default"){
		if(speechSynth) {
       	speechSynth.playTextAndResume(eTxt)
        sendtxt(eTxt)
        }
        else{
            if(sonos) {
                def sCommand = resumePlaying == true ? "playTrackAndResume" : "playTrackAndRestore"
                def sTxt = textToSpeech(eTxt instanceof List ? eTxt[0] : eTxt)
                def sVolume = settings.sonosVolume ?: 20
                sonos."${sCommand}"(sTxt.uri, sTxt.duration, sVolume)
                sendtxt(eTxt)
            }
        }
  	}
    else {
    if (actionType == "Triggered Report" && myProfile) {
    	eTxt = parent.runReport(myProfile)
        log.warn "eTxt = $eTxt"
    }
    def eProfile = app.label
    def nRoutine = false
	def stamp = state.lastTime = new Date(now()).format("h:mm aa", location.timeZone)     
	def today = new Date(now()).format("EEEE, MMMM d, yyyy", location.timeZone)
    if (getDayOk()==true && getModeOk()==true && getTimeOk()==true) {	
        if(eName == "time of day" && message){
                eTxt = message ? "$message".replace("&device", "${eDisplayN}").replace("&event", "routine").replace("&action", "executed").replace("&date", "${today}").replace("&time", "${stamp}").replace("&profile", "${eProfile}") : null
                    if(actionType == "Custom with Weather") eTxt = getWeatherVar(eTxt)
        }
        if(eName == "coolingSetpoint" || eName == "heatingSetpoint") {
            eVal = evt.value.toFloat()
            eVal = Math.round(eVal)
        }
        if(eName == "routineExecuted" && myRoutine) {
        	def deviceMatch = myRoutine?.find {r -> r == eDisplayN}  
            if (deviceMatch){
            	eTxt = message ? "$message".replace("&device", "${eDisplayN}").replace("&event", "routine").replace("&action", "executed").replace("&date", "${today}").replace("&time", "${stamp}").replace("&profile", "${eProfile}") : null
                    if(actionType == "Custom with Weather") eTxt = getWeatherVar(eTxt)
                    if (message){
						if(recipients?.size()>0 || sms?.size()>0) {
                    		sendtxt(eTxt)
                		}
                    takeAction(eTxt)
                	}
                    else {
                        eTxt = "routine was executed"
                        takeAction(eTxt) 
                    }
         	}
        }
        else {
            if(eName == "mode" && myMode) {
                def deviceMatch = myMode?.find {m -> m == eVal}  
                if (deviceMatch){
                    eTxt = message ? "$message".replace("&device", "${eVal}").replace("&event", "${eName}").replace("&action", "changed").replace("&date", "${today}").replace("&time", "${stamp}").replace("&profile", "${eProfile}") : null
                    if(actionType == "Custom with Weather") eTxt = getWeatherVar(eTxt)
                    if (message){
                        if(recipients?.size()>0 || sms?.size()>0) {
                            sendtxt(eTxt)
                        }
                        takeAction(eTxt)
                    }
                    else {
                        eTxt = "location mode has changed"
                        takeAction(eTxt) 
                    }
                }
            }        
            else {
                if (message || actionType == "Triggered Report"){      
                    if(message){
                    eTxt = message ? "$message".replace("&device", "${eDev}").replace("&event", "${eName}").replace("&action", "${eVal}").replace("&date", "${today}").replace("&time", "${stamp}").replace("&profile", "${eProfile}") : null
                    if(actionType == "Custom with Weather") eTxt = getWeatherVar(eTxt)
                    }
                    if(eTxt){
                        if(recipients?.size()>0 || sms?.size()>0) {
                            sendtxt(eTxt)
                        }
                        takeAction(eTxt)
                    }
                }
                else {
                    if (eDev == "weather"){eTxt = eName}
                    log.info "sending message: $eTxt"
                    takeAction(eTxt)
                }
            }
        }
	}
}
}
/***********************************************************************************************************************
    CUSTOM WEATHER VARIABLES
***********************************************************************************************************************/
private getWeatherVar(eTxt){
	def result
    // weather variables
	def weatherToday = mGetWeatherVar("today")
	def weatherTonight = mGetWeatherVar("tonight")
    def weatherTomorrow = mGetWeatherVar("tomorrow")
    def tHigh = mGetWeatherVar("high")
    def tLow = mGetWeatherVar("low")
    def tUV = mGetWeatherElements("uv")
    def tPrecip = mGetWeatherElements("precip")
    def tHum = mGetWeatherElements("hum")
    def tCond = mGetWeatherElements("cond")
    
    result = eTxt.replace("&today", "${weatherToday}").replace("&tonight", "${weatherTonight}").replace("&tomorrow", "${weatherTomorrow}")
	if(result) result = result.replace("&high", "${tHigh}").replace("&low", "${tLow}").replace("&wind", "${tWind}").replace("&uv", "${tUV}").replace("&precipitation", "${tPrecip}")
	if(result) result = result.replace("&humidity", "${tHum}").replace("&conditions", "${tCond}")

return result
}
/***********************************************************************************************************************
    CUSTOM SOUNDS HANDLER
***********************************************************************************************************************/
private takeAction(eTxt) {
	def sVolume
    def sTxt
    if(myProfile && actionType != "Triggered Report") runProfileActions()   
    if (actionType == "Custom" || actionType == "Custom with Weather" || actionType == "Triggered Report") {
		//state.sound = textToSpeech(eTxt instanceof List ? eTxt[0] : eTxt) // Retired to use direct variable Bobby 3/13/2017
        sTxt = textToSpeech(eTxt instanceof List ? eTxt[0] : eTxt)
    log.warn "sTxt is $sTxt, eTxt is $eTxt"
    }
    else loadSound()
    //Playing Audio Message
        if (speechSynth) {
            def currVolLevel = speechSynth.latestValue("level")
            def currMute = speechSynth.latestValue("mute")
                log.debug "vol switch = ${currVolSwitch}, vol level = ${currVolLevel}, currMute = ${currMute} "
                sVolume = settings.speechVolume ?: 30 
                speechSynth?.playTextAndResume(eTxt, sVolume)
                log.info "Playing message on the speech synthesizer'${speechSynth}' at volume '${sVolume}'"
        }
        if (sonos) { 
            def currVolLevel = sonos.latestValue("level") //as Integer
            currVolLevel = currVolLevel[0]
            def currMuteOn = sonos.latestValue("mute").contains("muted")
                log.debug "current vol level = ${currVolLevel}, muted = ${currMuteOn} "
                if (currMuteOn) { 
                    log.warn "speaker is on mute, sending unmute command"
                    sonos.unmute()
                }
                sVolume = settings.sonosVolume ?: 20
                sVolume = sVolume == 20 && currVolLevel == 0 ? sVolume : currVolLevel
                def elapsed = now() - state.lastPlayed
                log.warn "elapsed = $elapsed"
                def sCommand = resumePlaying == true ? "playTrackAndResume" : "playTrackAndRestore"
                //playTrackAndRestore(state.sound.uri, state.sound.duration, desiredVolume)
                    if(elapsed < 5000 ){
                        //state.sound = sTxt // backup
                        //def delaySound = Math.max((state.sound.duration as Integer),3) // backup
                        log.error "message is already playing, delaying new message by 2 seconds"
                		//runIn(delaySound, "delayMessage") // backup
						//sonos?.playTrackAndResume(sTxt.uri, Math.max((sTxt.duration as Integer),2), sVolume, [delay: 2000])
						sonos?."${sCommand}"(sTxt.uri, Math.max((sTxt.duration as Integer),2), sVolume, [delay: 2000])
                        state.lastPlayed = now()
                	}
                    else {                
                		sonos?."${sCommand}"(sTxt.uri, Math.max((sTxt.duration as Integer),2), sVolume)
                        //sonos?.playTrackAndResume(sTxt.uri, Math.max((sTxt.duration as Integer),2), sVolume)  
                        //state.sound = sTxt // backup
                        state.lastPlayed = now()
                	}
                log.info "Playing $sTxt on the music player $sonos at volume $sVolume"
        }
}
def delayMessage() {
	log.warn "delay fired"
	def sVolume = settings.sonosVolume ?: 20
		sonos?.playTrackAndResume(state.sound.uri, Math.max((state.sound.duration as Integer),2), sVolume)
		state.sound = null
}
/***********************************************************************************************************************
    WEATHER ALERTS
***********************************************************************************************************************/
def mGetWeatherAlerts(){
	def result = "There are no weather alerts for your area"
	def data = [:]
    try {
		if (getDayOk()==true && getModeOk()==true && getTimeOk()==true) {
        	def weather = getWeatherFeature("alerts", settings.wZipCode)
        	def type = weather.alerts.type[0]
            def alert = weather.alerts.description[0]
            def expire = weather.alerts.expires[0]
            def typeOk = myWeatherAlert?.find {a -> a == type}
			if(typeOk){
                if(expire != null) expire = expire?.replaceAll(~/ EST /, " ").replaceAll(~/ CST /, " ").replaceAll(~/ MST /, " ").replaceAll(~/ PST /, " ")
                if(alert != null) {
                    result = alert  + " is in effect for your area, that expires at " + expire
                    if(state.weatherAlert == null){
                        state.weatherAlert = result
                        state.lastAlert = new Date(now()).format("h:mm aa", location.timeZone)
                        data = [value:"alert", name: result, device:"weather"] 
                        alertsHandler(data)
                        if (push){
							sendtxt(data)
    						log.info "sent from the Weather Alerts handler"
							}
                    	}
                    else {
                        log.warn "new weather alert = ${alert} , expire = ${expire}"
                        def newAlert = result != state.weatherAlert ? true : false
                        if(newAlert == true){
                            state.weatherAlert = result
                            state.lastAlert = new Date(now()).format("h:mm aa", location.timeZone)
                            data = [value:"alert", name: result, device:"weather"] 
                            alertsHandler(data)
                        }
                    }
                }
         	}
            log.warn "weather alert not matched"
    	}
    }
	catch (Throwable t) {
	log.error t
	return result
	}
}
/***********************************************************************************************************************
    HOURLY FORECAST
***********************************************************************************************************************/
def mGetCurrentWeather(){
    def weatherData = [:]
    def data = [:]
   	def result
    try {
		if (getDayOk()==true && getModeOk()==true && getTimeOk()==true) {
        //hourly updates
            def cWeather = getWeatherFeature("hourly", settings.wZipCode)
            def cWeatherCondition = cWeather.hourly_forecast[0].condition
            def cWeatherPrecipitation = cWeather.hourly_forecast[0].pop + " percent"
            def cWeatherWind = cWeather.hourly_forecast[0].wspd.english + " miles per hour"
            def cWeatherHum = cWeather.hourly_forecast[0].humidity + " percent"
            def cWeatherUpdate = cWeather.hourly_forecast[0].FCTTIME.civil
            //past hour's data
            def pastWeather = state.lastWeather
            //current forecast
				weatherData.wCond = cWeatherCondition
                weatherData.wWind = cWeatherWind
                weatherData.wHum = cWeatherHum
                weatherData.wPrecip = cWeatherPrecipitation
            //last weather update
            def lastUpdated = new Date(now()).format("h:mm aa", location.timeZone)
            if(myWeather) {
                if(pastWeather == null) {
                    state.lastWeather = weatherData
                    state.lastWeatherCheck = lastUpdated
                    result = "hourly weather forcast notification has been activated at " + lastUpdated + " You will now receive hourly weather updates, only if the forecast data changes" 
                    data = [value:"precipitation", name: result, device:"weather"]
                    alertsHandler(data)
                }
                else {
                    def wUpdate = pastWeather.wCond != cWeatherCondition ? "current weather condition" : pastWeather.wWind != cWeatherWind ? "wind intensity" : pastWeather.wHum != cWeatherHum ? "humidity" : pastWeather.wPrecip != cWeatherPrecipitation ? "chance of precipitation" : null
                    def wChange = wUpdate == "current weather condition" ? cWeatherCondition : wUpdate == "wind intensity" ? cWeatherWind  : wUpdate == "humidity" ? cWeatherHum : wUpdate == "chance of precipitation" ? cWeatherPrecipitation : null                    
                    //something has changed
                    if(wUpdate != null){
                        // saving update to state
                        state.lastWeather = weatherData
                        state.lastWeatherCheck = lastUpdated
                        if (myWeather == "Any Weather Updates"){
                        	def condChanged = pastWeather.wCond != cWeatherCondition
                            def windChanged = pastWeather.wWind != cWeatherWind
                            def humChanged = pastWeather.wHum != cWeatherHum
                            def precChanged = pastWeather.wPrecip != cWeatherPrecipitation
							if(condChanged){
                            	result = "The hourly weather forecast has been updated. The weather condition has been changed to "  + cWeatherCondition
                            }
                            if(windChanged){
                            	if(result) {result = result +  " , the wind intensity to "  + cWeatherWind }
                            	else result = "The hourly weather forecast has been updated. The wind intensity has been changed to "  + cWeatherWind
							}
                            if(humChanged){
                            	if(result) {result = result +  " , the humidity to "  + cWeatherHum }
                            	else result = "The hourly weather forecast has been updated. The humidity has been changed to "  + cWeatherHum
							}
                            if(precChanged){
                            	if(result) {result = result + " , the chance of rain to "  + cWeatherPrecipitation }
                            	else result = "The hourly weather forecast has been updated. The chance of rain has been changed to "  + cWeatherPrecipitation
                            }
                            if (push){
								sendtxt(result)
    							log.info "sent from the hourly forcast handler"
							}
                            data = [value:"forecast", name: result, device:"weather"]  
                            alertsHandler(data)
                        }
                        else {
                            if (myWeather == "Weather Condition Changes" && wUpdate ==  "current weather condition"){
                                result = "The " + wUpdate + " has been updated to " + wChange
                                data = [value:"condition", name: result, device:"weather"]  
                                alertsHandler(data)
                            }
                            else if (myWeather == "Chance of Precipitation Changes" && wUpdate ==  "chance of precipitation"){
                                result = "The " + wUpdate + " has been updated to " + wChange
                                data = [value:"precipitation", name: result, device:"weather"] 
                                alertsHandler(data)
                            }        
                            else if (myWeather == "Wind Speed Changes" && wUpdate == "wind intensity"){
                                result = "The " + wUpdate + " has been updated to " + wChange
                                data = [value:"wind", name: result, device:"weather"] 
                                alertsHandler(data)
                            }         
                            else if (myWeather == "Humidity Changes" && wUpdate == "humidity"){
                                result = "The " + wUpdate + " has been updated to " + wChange
                                data = [value:"humidity", name: result, device:"weather"] 
                                alertsHandler(data)
                            }
						}
                    }       
                }
                log.info "refreshed hourly weather forecast: past forecast = ${pastWeather}; new forecast = ${weatherData}"  
            }
    	}
    }
	catch (Throwable t) {
	log.error t
	return result
	}  
}
/***********************************************************************************************************************
    WEATHER ELEMENTS
***********************************************************************************************************************/
def private mGetWeatherElements(element){
	state.pTryAgain = false
    def result ="Current weather is not available at the moment, please try again later"
   	try {
        //hourly updates
        def cWeather = getWeatherFeature("hourly", settings.wZipCode)
        def cWeatherCondition = cWeather.hourly_forecast[0].condition
        def cWeatherPrecipitation = cWeather.hourly_forecast[0].pop + " percent"
        def cWeatherWind = cWeather.hourly_forecast[0].wspd.english + " miles per hour"
        def cWeatherHum = cWeather.hourly_forecast[0].humidity + " percent"
        def cWeatherUpdate = cWeather.hourly_forecast[0].FCTTIME.civil
        
        def condWeather = getWeatherFeature("conditions", settings.wZipCode)
        def condTodayUV = condWeather.current_observation.UV
        
        if(debug) log.debug "cWeatherUpdate = ${cWeatherUpdate}, cWeatherCondition = ${cWeatherCondition}, " +
        					"cWeatherPrecipitation = ${cWeatherPrecipitation}, cWeatherWind = ${cWeatherWind},  cWeatherHum = ${cWeatherHum}, cWeatherHum = ${condTodayUV}  "    
/*
        if(wMetric){
        //hourly metric updates
        def cWeatherWind_m = cWeather.hourly_forecast[0].wspd.metric + " kilometers per hour"        
        	if		(element == "precip" || element == "rain") {result = "The chance of precipitation is " + cWeatherPrecipitation }
        	else if	(element == "wind") {result = "The wind intensity is " + cWeatherWind_m }
        	else if	(element == "uv") {result = "The UV index is " + condTodayUV }
			else if	(element == "hum") {result = "The relative humidity is " + cWeatherHum }        
			else if	(element == "cond") {result = "The current weather condition is " + cWeatherCondition }
        }
        else{
        
*/
        
        	if		(element == "precip" ) {result = "The chance of precipitation is " + cWeatherPrecipitation }
        	else if	(element == "wind") {result = "The wind intensity is " + cWeatherWind }
        	else if	(element == "uv") {result = "The UV index is " + condTodayUV }
			else if	(element == "hum") {result = "The relative humidity is " + cWeatherHum }        
			else if	(element == "cond") {result = "The current weather condition is " + cWeatherCondition }        

	return result
	}
	catch (Throwable t) {
		log.error t
        state.pTryAgain = true
        return result
	} 
}
/***********************************************************************************************************************
    WEATHER TEMPS
***********************************************************************************************************************/
def private mGetWeatherVar(var){
	state.pTryAgain = false
    def result
	try {
		def weather = getWeatherFeature("forecast", settings.wZipCode)
        def sTodayWeather = weather.forecast.simpleforecast.forecastday[0]
        if(var =="high") result = sTodayWeather.high.fahrenheit//.toInteger()
        if(var == "low") result = sTodayWeather.low.fahrenheit//.toInteger()
        if(var =="today") result = 	weather.forecast.txt_forecast.forecastday[0].fcttext 
        if(var =="tonight") result = weather.forecast.txt_forecast.forecastday[1].fcttext 
		if(var =="tomorrow") result = weather.forecast.txt_forecast.forecastday[2].fcttext 

        
/*
	if(wMetric){
                if(tHigh) result = weather.forecast.simpleforecast.forecastday[0].high.celsius//.toInteger()
                if(tLow) result = weather.forecast.simpleforecast.forecastday[0].low.celsius//.toInteger()
            }
            else {
                result = "Today's low temperature is: " + tLow  + ", with a high of " + tHigh
        	}
 */           
            return result
	}
	catch (Throwable t) {
        log.error t
        state.pTryAgain = true
        return result
    }
} 
/***********************************************************************************************************************
    CRON HANDLER
***********************************************************************************************************************/
def cronHandler(var) {
	def result
		if(var == "Minutes") {
        //	0 0/3 * 1/1 * ? *
        	if(xMinutes) result = "0 0/${xMinutes} * 1/1 * ? *"
            else log.error " unable to schedule your reminder due to missing required variables"
        }
		if(var == "Hourly") {
        //	0 0 0/6 1/1 * ? *
			if(xHours) result = "0 0 0/${xHours} 1/1 * ? *"
            else log.error " unable to schedule your reminder due to missing required variables"
		}
		if(var == "Daily") {
        // 0 0 1 1/7 * ? *
            def hrmn = hhmm(xDaysStarting, "HH:mm")
            def hr = hrmn[0..1] 
            def mn = hrmn[3..4]
        	if(xDays) result = "0 $mn $hr 1/${xDays} * ? *"
            if(xDaysWeekDay && xDaysStarting) result = "0 $mn $hr 1/${xDays} * MON-FRI *"
            else log.error " unable to schedule your reminder due to missing required variables"
		}
        if(var == "Weekly") {
        // 	0 0 2 ? * TUE,SUN *
        	def hrmn = hhmm(xWeeksStarting, "HH:mm")
            def hr = hrmn[0..1]
            def mn = hrmn[3..4]
            def weekDaysList = [] 
            	xWeeks?.each {weekDaysList << it }
            def weekDays = weekDaysList.join(",")
            if(xWeeks && xWeeksStarting) { result = "0 $mn $hr ? * ${weekDays} *" }
            else log.error " unable to schedule your reminder due to missing required variables"
		}
		if(var == "Monthly") { 
        // 0 30 5 6 1/2 ? *
        	def hrmn = hhmm(xMonthsStarting, "HH:mm")
            def hr = hrmn[0..1]
            def mn = hrmn[3..4]
        	if(xMonths && xMonthsDay) { result = "0 $mn $hr ${xMonthsDay} 1/${xMonths} ? *"}
            else log.error "unable to schedule your reminder due to missing required variables"
		}
		if(var == "Yearly") {
        //0 0 4 1 4 ? *
        	def hrmn = hhmm(xYearsStarting, "HH:mm")
            def hr = hrmn[0..1]
            def mn = hrmn[3..4]           
        	if(xYears) {result = "0 $mn $hr ${xYearsDay} ${xYears} ? *"}
            else log.error "unable to schedule your reminder due to missing required variables"
		}
    log.info "scheduled $var recurring event" //time period with expression: $result"
    schedule(result, "scheduledTimeHandler")
}
/***********************************************************************************************************************
    RESTRICTIONS HANDLER
***********************************************************************************************************************/
private getAllOk() {
	modeOk && daysOk && timeOk
}
private getModeOk() {
    def result = !modes || modes?.contains(location.mode)
	log.debug "modeOk = $result"
    result
} 
private getDayOk() {
    def result = true
if (days) {
		def df = new java.text.SimpleDateFormat("EEEE")
		if (location.timeZone) {
			df.setTimeZone(location.timeZone)
		}
		else {
			df.setTimeZone(TimeZone.getTimeZone("America/New_York"))
		}
		def day = df.format(new Date())
		result = days.contains(day)
	}
	log.debug "daysOk = $result"
	result
}
private getTimeOk() {
	def result = true
	if ((starting && ending) ||
	(starting && endingX in ["Sunrise", "Sunset"]) ||
	(startingX in ["Sunrise", "Sunset"] && ending) ||
	(startingX in ["Sunrise", "Sunset"] && endingX in ["Sunrise", "Sunset"])) {
		def currTime = now()
		def start = null
		def stop = null
		def s = getSunriseAndSunset(zipCode: zipCode, sunriseOffset: startSunriseOffset, sunsetOffset: startSunsetOffset)
		if(startingX == "Sunrise") start = s.sunrise.time
		else if(startingX == "Sunset") start = s.sunset.time
		else if(starting) start = timeToday(starting,location.timeZone).time
		s = getSunriseAndSunset(zipCode: zipCode, sunriseOffset: endSunriseOffset, sunsetOffset: endSunsetOffset)
		if(endingX == "Sunrise") stop = s.sunrise.time
		else if(endingX == "Sunset") stop = s.sunset.time
		else if(ending) stop = timeToday(ending,location.timeZone).time
		result = start < stop ? currTime >= start && currTime <= stop : currTime <= stop || currTime >= start
	if (parent.debug) log.trace "getTimeOk = $result."
    }
    log.debug "timeOk = $result"
    return result
}
private hhmm(time, fmt = "h:mm a") {
	def t = timeToday(time, location.timeZone)
	def f = new java.text.SimpleDateFormat(fmt)
	f.setTimeZone(location.timeZone ?: timeZone(time))
	f.format(t)
}
private offset(value) {
	def result = value ? ((value > 0 ? "+" : "") + value + " min") : ""
}
private timeIntervalLabel() {
	def result = ""
	if      (startingX == "Sunrise" && endingX == "Sunrise") result = "Sunrise" + offset(startSunriseOffset) + " to Sunrise" + offset(endSunriseOffset)
	else if (startingX == "Sunrise" && endingX == "Sunset") result = "Sunrise" + offset(startSunriseOffset) + " to Sunset" + offset(endSunsetOffset)
	else if (startingX == "Sunset" && endingX == "Sunrise") result = "Sunset" + offset(startSunsetOffset) + " to Sunrise" + offset(endSunriseOffset)
	else if (startingX == "Sunset" && endingX == "Sunset") result = "Sunset" + offset(startSunsetOffset) + " to Sunset" + offset(endSunsetOffset)
	else if (startingX == "Sunrise" && ending) result = "Sunrise" + offset(startSunriseOffset) + " to " + hhmm(ending, "h:mm a z")
	else if (startingX == "Sunset" && ending) result = "Sunset" + offset(startSunsetOffset) + " to " + hhmm(ending, "h:mm a z")
	else if (starting && endingX == "Sunrise") result = hhmm(starting) + " to Sunrise" + offset(endSunriseOffset)
	else if (starting && endingX == "Sunset") result = hhmm(starting) + " to Sunset" + offset(endSunsetOffset)
	else if (starting && ending) result = hhmm(starting) + " to " + hhmm(ending, "h:mm a z")
}
/***********************************************************************************************************************
    SMS HANDLER
***********************************************************************************************************************/
private void sendtxt(message) {
	def stamp = state.lastTime = new Date(now()).format("h:mm aa", location.timeZone)
    if (parent.debug) log.debug "Request to send sms received with message: '${message}'"
    if (sendContactText) { 
        sendNotificationToContacts(message, recipients)
            if (parent.debug) log.debug "Sending sms to selected reipients"
    } 
    else {
    	if (push) {
        	message = timeStamp==true ? message + " at " + stamp : message
    		sendPush message
            	if (parent.debug) log.debug "Sending push message to selected reipients"
        }
    } 
    if (notify) {
        sendNotificationEvent(message)
             	if (parent.debug) log.debug "Sending notification to mobile app"
    }
    if (sms) {
        sendText(sms, message)
        if (parent.debug) log.debug "Processing message for selected phones"
	}
}
private void sendText(number, message) {
    if (sms) {
        def phones = sms.split("\\,")
        for (phone in phones) {
            sendSms(phone, message)
            if (parent.debug) log.debug "Sending sms to selected phones"
        }
    }
}
def runProfileActions() {
              	def String pintentName = (String) null
                def String pContCmdsR = (String) null
                def String ptts = (String) null
                //def childMasterApp
                //childMasterApp = child.app.name
        		def pContCmds = false
        		def pTryAgain = false
        		def dataSet = [:]
           		parent.childApps.each {child ->
                        def ch = child.label
                		if (ch == settings.myProfile) { 
                    		if (debug) log.debug "Matched the profile"
                            pintentName = child.label
                    		ptts = "Running Profile actions from the Notification Add-on"
                            dataSet = [ptts:ptts, pintentName:pintentName]
                            child.profileEvaluate(dataSet)
						}
            	}
                
}
/***********************************************************************************************************************
    CUSTOM SOUNDS HANDLER
***********************************************************************************************************************/
private loadSound() {
	switch (actionType) {
		case "Bell 1":
			state.sound = [uri: "http://s3.amazonaws.com/smartapp-media/sonos/bell1.mp3", duration: "10"]
			break;
		case "Bell 2":
			state.sound = [uri: "http://s3.amazonaws.com/smartapp-media/sonos/bell2.mp3", duration: "10"]
			break;
		case "Dogs Barking":
			state.sound = [uri: "http://s3.amazonaws.com/smartapp-media/sonos/dogs.mp3", duration: "10"]
			break;
		case "Fire Alarm":
			state.sound = [uri: "http://s3.amazonaws.com/smartapp-media/sonos/alarm.mp3", duration: "17"]
			break;
		case "The mail has arrived":
			state.sound = [uri: "http://s3.amazonaws.com/smartapp-media/sonos/the+mail+has+arrived.mp3", duration: "1"]
			break;
		case "A door opened":
			state.sound = [uri: "http://s3.amazonaws.com/smartapp-media/sonos/a+door+opened.mp3", duration: "1"]
			break;
		case "There is motion":
			state.sound = [uri: "http://s3.amazonaws.com/smartapp-media/sonos/there+is+motion.mp3", duration: "1"]
			break;
		case "Smartthings detected a flood":
			state.sound = [uri: "http://s3.amazonaws.com/smartapp-media/sonos/smartthings+detected+a+flood.mp3", duration: "2"]
			break;
		case "Smartthings detected smoke":
			state.sound = [uri: "http://s3.amazonaws.com/smartapp-media/sonos/smartthings+detected+smoke.mp3", duration: "1"]
			break;
		case "Someone is arriving":
			state.sound = [uri: "http://s3.amazonaws.com/smartapp-media/sonos/someone+is+arriving.mp3", duration: "1"]
			break;
		case "Piano":
			state.sound = [uri: "http://s3.amazonaws.com/smartapp-media/sonos/piano2.mp3", duration: "10"]
			break;
		case "Lightsaber":
			state.sound = [uri: "http://s3.amazonaws.com/smartapp-media/sonos/lightsaber.mp3", duration: "10"]
			break;
		default:
			state?.sound = [uri: "http://s3.amazonaws.com/smartapp-media/sonos/bell1.mp3", duration: "10"]
			break;
	}
}
/************************************************************************************************************
   PROFILES 
************************************************************************************************************/       
def getProfileList(){        
		return parent.getChildApps()*.label.sort()
}
/************************************************************************************************************
   Page status and descriptions 
************************************************************************************************************/       
def pSendSettings() {def result = ""
    if (sendContactText || sendText || push) {
    	result = "complete"}
   		result}
def pSendComplete() {def text = "Tap here to configure settings" 
    if (sendContactText || sendText || push) {
    	text = "Configured"}
    	else text = "Tap to Configure"
		text}
def triggersSettings() {def result = ""
    if (myWeather || myWeatherAlert || myWater || mySmoke || myPresence || myMotion || myContact || mySwitch || myPower || myLocks || myTstat || myMode || myRoutine || frequency) {
    	result = "complete"}
   		result}
def triggersComplete() {def text = "Tap here to configure settings" 
    if (myWeather || myWeatherAlert || myWater || mySmoke || myPresence || myMotion || myContact || mySwitch || myPower || myLocks || myTstat || myMode || myRoutine || frequency) {
    	text = "Configured"}
    	else text = "Tap to Configure"
		text} 

def pRestSettings() {def result = ""
    if (modes || days) {
    	result = "complete"}
   		result}
def pRestComplete() {def text = "Tap here to configure settings" 
    if (modes || days) {
    	text = "Configured"}
    	else text = "Tap to Configure"
		text}     
def pTimeSettings() {def result = ""
    if (startingX || endingX) {
    	result = "complete"}
   		result}
def pTimeComplete() {def text = "Tap here to configure settings" 
    if (startingX || endingX) {
    	text = "Configured"}
    	else text = "Tap to Configure"
		text}