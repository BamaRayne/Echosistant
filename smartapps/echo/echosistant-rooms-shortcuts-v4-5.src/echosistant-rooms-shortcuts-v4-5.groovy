/* 
* EchoSistant Shortcuts - Child app 
*
*		12/27/2017		Version:1.0 R.0.0.6		Added delay off for switches when contact closes
*		12/23/2017		Version:1.0 R.0.0.5		Added trigger --> Dimmer on, off, equal to, less than, greater than
*												Added Action/trigger --> Contact open/close turn on/off switch
*												Added Restrictions --> mode, day of week, time, sunrise & sunset
*												Made multiple UI changes
*		12/18/2017		Version:1.0 R.0.0.4		Added action --> Lights toggle 
*		12/15/2017		Version:1.0 R.0.0.3		Added trigger --> Locks locked/unlocked
* 		12/7/2017		Version:1.0 R.0.0.2		Bug Fix
*		11/29/2017		Version:1.0 R.0.0.1a	Bug fix: other switches not working
*		11/27/2017		Version:1.0 R.0.0.1		Initital Release
*		
* 
*  Copyright 2017 Jason Headley, Bobby Dobrescu, Corey Lista, & Anthony Santilli 
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
  name            : "EchoSistant Rooms Shortcuts v4.5",
  namespace       : "Echo",
  author          : "JH/BD",
  description     : "Shortcuts to make Alexa better!.",
  category        : "", 
  parent          : "Echo:EchoSistant Rooms v4.5",
	iconUrl			: "https://raw.githubusercontent.com/BamaRayne/Echosistant/master/smartapps/bamarayne/echosistant.src/app-Echosistant.png",
	iconX2Url		: "https://raw.githubusercontent.com/BamaRayne/Echosistant/master/smartapps/bamarayne/echosistant.src/app-Echosistant@2x.png",
	iconX3Url		: "https://raw.githubusercontent.com/BamaRayne/Echosistant/master/smartapps/bamarayne/echosistant.src/app-Echosistant@2x.png")
/**********************************************************************************************************************************************/
private def appVerDate() { def text = "12-27-2017" }
private def textVersion() { def text = "1.0" }
private release() { def text = "R.0.0.6" }
/******************************************************************************
MAIN PROFILE PAGE
******************************************************************************/
preferences {
    if (app.label == null) {
        page(name: "mainProfilePage", title: "", nextPage: "secondPage", uninstall: true) {
            section ("Name this Shortcut") {
                label title:"Shortcut Name", required:true
            }
            section ("") {
                href "renamePage", title: "Verbal Triggers and Alexa Responses..."  
            }
        }
    }
    else if (app.label != null) {
        page(name: "secondPage", title: "", install: true, uninstall: true) {
            section ("") {
                href "renamePage", title: "Verbal Triggers and Alexa Responses..."  
            }
            section ("") {
                href "triggers", title: "Physical Device Triggers", description: pTriggerComplete(), state: pTriggerSettings()
            }
            section ("") {
                href "sDevices", title: "Shortcut Actions", description: pDevicesComplete(), state: pDevicesSettings()
            }
            section ("") {
                href "pRestrict", title: "General Shortcut Restrictions", description: pRestrictComplete(), state: pRestrictSettings()
            }
            section ("") {
                paragraph ("Child App Version: ${textVersion()} | Release: ${release()} | Date: ${appVerDate()}")
            }
        }
    }
}
page(name: "renamePage", title: "", nextPage: "secondPage", uninstall: true) {
    section ("") {
        label title: "When you say...", required: true//, submitOnChange: true
        input "scResponse", "text", title: "Alexa responds with...", required: false//, submitOnChange: true	
    }
    section ("") {
        input "alias1", "text", title: "You can also say this...", required: false//, submitOnChange: true
        input "scResponse1", "text", title: "...and, Alexa responds with this", required: false//, submitOnChange: true
    }
    section ("") {  
        input "alias2", "text", title: "or, You can say this...", required: false//, submitOnChange: true
        input "scResponse2", "text", title: "...and, Alexa responds with this", required: false//, submitOnChange: true
    }
    section ("") {
        input "alias3", "text", title: "or, You can say this...", required: false//, submitOnChange: true
        input "scResponse3", "text", title: "...and, Alexa responds with this", required: false//, submitOnChange: true
    }
    section ("") {  
        input "alias4", "text", title: "or, You can say this...", required: false//, submitOnChange: true
        input "scResponse4", "text", title: "...and, Alexa responds with this", required: false//, submitOnChange: true
    }
    section ("") {
        input "mAlias", "bool", title: "Do you need even more phrases?", defaultValue: false, required: false, submitOnChange: true
    }
    if (mAlias) {
        section ("") {
            input "alias5", "text", title: "or, You can say this...", required: false//, submitOnChange: true
            input "scResponse5", "text", title: "...and, Alexa responds with this", required: false//, submitOnChange: true
        }
        section ("") {  
            input "alias6", "text", title: "or, You can say this...", required: false//, submitOnChange: true
            input "scResponse6", "text", title: "...and, Alexa responds with this", required: false//, submitOnChange: true
        }
        section ("") {
            input "alias7", "text", title: "or, You can say this...", required: false//, submitOnChange: true
            input "scResponse7", "text", title: "...and, Alexa responds with this", required: false//, submitOnChange: true
        }
        section ("") {  
            input "alias8", "text", title: "or, You can say this...", required: false//, submitOnChange: true
            input "scResponse8", "text", title: "...and, Alexa responds with this", required: false//, submitOnChange: true
        }
        section ("") {  
            input "alias9", "text", title: "and finally, You can say this...", required: false//, submitOnChange: true
            input "scResponse9", "text", title: "...and, Alexa responds with this", required: false//, submitOnChange: true
        }
    }
    section ("") {
        paragraph ("Child App Version: ${textVersion()} | Release: ${release()} | Date: ${appVerDate()}")
    }
}
    	    
page name: "triggers"
def triggers() {
    dynamicPage(name: "triggers", title: "Will Execute...", hideWhenEmpty:true,install: false, uninstall: false) {
        section ("") {
            input "scMode", "mode", title: "When the Mode changes to...", multiple: true, required: false, submitOnChange: true
        }
        section ("") {
            input "scDim", "capability.switchLevel", title: "...or, when these dimmers...", multiple: true, submitOnChange: true, required: false
            if (scDim) {
                input "scDimCmd", "enum", title: "are changed to...", options:["on":"On","off":"Off","greater":"Greater than","lessThan":"Less than","equal":"Being equal to"], multiple: false, required: false, submitOnChange: true
                if (scDimCmd == "greater" ||scDimCmd == "lessThan" || scDimCmd == "equal") {
                    input "scDimLvl", "number", title: "...this level", range: "0..100", required: false, submitOnChange: true
                }
            }
        }
        section ("") {
            input "scTrigger", "capability.switch", title: "...or, when these switches...", multiple: true, submitOnChange: true, required:false
            if (scTrigger) {
                input "scTriggerCmd", "enum", title: "are turned...", options:["on":"On","off":"Off"], multiple: false, required: false
            }
        }
        section ("") {
            input "scContact", "capability.contactSensor", title: "...or, when these contacts...", multiple: true, required: false, submitOnChange: true
            if (scContact) {
                input "scContactS", "enum", title: "are changed to...", options: ["open", "closed"], required: false, submitOnChange: true
            }
        }
        section ("") {
            input "scMotion", "capability.motionSensor", title: "...or, when these motion sensors...", multiple: true, required: false, submitOnChange: true
            if (scMotion) {
                input "scMotionS", "enum", title: "change to...", options: ["active", "inactive"], required: false
            }
        }
        section ("") {
            input "scPresenceT", "capability.presenceSensor", title: "...or, when these presence sensors...", multiple: true, required: false, submitOnChange: true
            if (scPresenceT) {
                input "scPresenceTA", "enum", title: "change to...", options: ["arrived", "departed"], required: false
            }
        }
        section (""){
            input "scLocks", "capability.lock", title: "...or, when these locks...", hideWhenEmpty:true, multiple: true, submitOnChange: true
            if (scLocks) {
                input "scLocksCmd", "enum", title: "change to...", options:["lock", "unlock"], multiple: false, required: false, submitOnChange:true
            }
        }
    } 
}

/******************************************************************************
DEVICES SELECTION PAGE
******************************************************************************/
page name: "sDevices"
def sDevices() {
    dynamicPage(name: "sDevices", title: "will make...",install: false, uninstall: false) {
        section (""){
            input "sSwitches", "capability.switch", title: "...these lights and switches", multiple: true, required: false, submitOnChange: true 
            if (sSwitches) {
                input "sSwitchesCmd", "enum", title: "...will change to...", options:["on":"Turn on","off":"Turn off","toggle":"Toggle"], multiple: false, required: false, submitOnChange:true
                if (scContactS=="open" && sSwitchesCmd=="on") {
                    input "ContactOff", "bool", title: "Do you want to turn these switches off when the contact closes?", default: false, submitOnChange: true
                    if (ContactOff) {
                        input "cOffDelay", "number", title: "...but delay these switches turning off by this many seconds...", required: true, defaultValue:"0", submitOnChange: true 
                    }
                }
                if (scContactS=="closed" && sSwitchesCmd=="off") {
                    input "ContactOn", "bool", title: "Do you want to turn these switches on when the contact opens?", default: false, submitOnChange: true 
                }
            }
        }
        if (sSwitchesCmd) {
            section("") {
                input "sOtherSwitch", "capability.switch", title: "... and even more lights & switches", multiple: true, required: false, submitOnChange: true
                if (sOtherSwitch) {
                    input "sOtherSwitchCmd", "enum", title: "...will change to...", options: ["on":"Turn on","off":"Turn off","toggle":"Toggle"], multiple: false, required: false,
                        submitOnChange: true
                    if (scContactS=="open" && sOtherSwitchCmd=="on") {
                        input "oContactOff", "bool", title: "Do you want to turn these switches off when the contact closes?", default: false, submitOnChange: true
                        if (oContactOff) {
                            input "ocOffDelay", "number", title: "...but delay these switches turning off by this many seconds...", defaultValue: 0, required: true, submitOnChange: true 
                        }
                    }
                    if (scContactS=="closed" && sOtherSwitchCmd=="off") {
                        input "oContactOn", "bool", title: "Do you want to turn these switches on when the contact opens?", default: false, submitOnChange: true
                    }
                }
            }
        }
        section (""){
            input "sDimmers", "capability.switchLevel", title: "...and these dimmable lights & switches", multiple: true, required: false , submitOnChange:true
            if (sDimmers) {
                input "sDimmersCmd", "enum", title: "...will...", options:["on":"Turn on","off":"Turn off","set":"Set level","decrease":"Dim by %","increase":"Brighten by %"], multiple: false, required: false, submitOnChange: true
                if (sDimmersCmd=="decrease") {
                    input "sDimDecrease", "number", title: "the lights by this %", required: false, submitOnChange: true
                }
                if (sDimmersCmd == "increase") {
                    input "sDimIncrease", "number", title: "the lights by this %", required: false, submitOnChange: true
                }
                if (sDimmersCmd == "set") {
                    input "sDimmersLVL", "number", title: "...of the lights to...", description: "Set Dimmer Level", range: "0..100", required: false, submitOnChange: true
                }
            }
        }
        if (sDimmersCmd) {
            section("") {
                input "sOtherDimmers", "capability.switchLevel", title: "...and even more dimmable lights & switches", multiple: true, required: false , submitOnChange:true
                if (sOtherDimmers) {
                    input "sOtherDimmersCmd", "enum", title: "...will...", options:["on":"Turn on","off":"Turn off","set":"Set level","decrease":"Dim by %","increase":"Brighten by %"], multiple: false, required: false, submitOnChange:true
                    if (sOtherDimmersCmd=="decrease") {
                        input "sOtherDimDecrease", "number", title: "the lights by this %", required: false, submitOnChange: true
                    }
                    if (sOtherDimmersCmd == "increase") {
                        input "sOtherDimIncrease", "number", title: "the lights by this %", required: false, submitOnChange: true
                    }
                    if (sOtherDimmersCmd == "set") {
                        input "sOtherDimmersLVL", "number", title: "...of the lights to...", description: "Set Level...", range: "0..100", required: false, submitOnChange: true
                    }
                }
            }
        }
        section (""){
            input "sHues", "capability.colorControl", title: "...and these color lights", multiple: true, required: false, submitOnChange:true
            if (sHues) {
                input "sHuesCmd", "enum", title: "...will...", options:["on":"Turn on","off":"Turn off","setColor":"Set Color"], multiple: false, required: false,
                    submitOnChange:true
                if(sHuesCmd == "setColor") {
                    input "sHuesColor", "enum", title: "...set the color to...", required: false, multiple:false, options: fillColorSettings()?.name
                }
                //                        if(sHuesCmd == "setColor" || sHuesCmd == "on") {
                //                            input "sHuesLevel", "enum", title: "Light Level?", required: false, range: "0..100", options: getSetLevelEnum(), submitOnChange:true
                //                        }
            }
        }
        if (sHuesLevel) {
            section("") {
                input "sHuesOther", "capability.colorControl", title: "...and even more color lights", multiple: true, required: false, submitOnChange:true
                if (sHuesOther) {
                    input "sHuesOtherCmd", "enum", title: "...will...", options:["on":"Turn on","off":"Turn off","setColor":"Set Color"], multiple: false, required: false,
                        submitOnChange:true
                    if(sHuesOtherCmd == "setColor") {
                        input "sHuesOtherColor", "enum", title: "...set the color to...", required: false, multiple:false, options: fillColorSettings()?.name
                    }
                                                  if(sHuesOtherCmd == "on" || sHuesOtherCmd == "setColor") {
                                                      input "sHuesOtherLevel", "enum", title: "Light Level?", required: false, range: "0..100", options: getSetLevelEnum()
                                                  }
                }
            }
        }
        section (""){
            input "mSwitchesOn", "capability.switch", title: "...will delay turning these switches on...", multiple: true, required: false, submitOnChange: true
            if (mSwitchesOn) {
                input "mOn", "number", title: "...by this many seconds", required: true, defaultValue: 3, submitOnChange: true
            }
            input "mSwitchesOff", "capability.switch", title: "...will delay turning these swtiches off...", multiple: true, required: false, submitOnChange: true
            if (mSwitchesOff) {
                input "mOff", "number", title: "...by this many seconds", required: true, defaultValue: 3, submitOnChange: true
            }
        }
                section ("") {
input "sFlash", "capability.switch", title: "Flash These Switch(es)", multiple: true, required: false, submitOnChange:true
if (sFlash) {
input "numFlashes", "number", title: "This number of times (default 3)", required: false, submitOnChange:true
input "onFor", "number", title: "On for (default 1 second)", required: false, submitOnChange:true
input "offFor", "number", title: "Off for (default 1 second)", required: false, submitOnChange:true
}
}
        section ("") {
    input "sFans", "capability.switchLevel", title: "...and these ceiling fans...", multiple: true, required: false, submitOnChange: true
    if (sFans) {
        input "sFansCmd", "enum", title: "...will...", options:["on":"Turn on","off":"Turn off","low":"Low","med":"Med","high":"High","incr":"Speed Up","decr":"Slow Down"], multiple: false, required: false, submitOnChange:true
        if (sFansCmd == "incr") {
            input "sFanIncr", "number", title: "Increase the fan by this %", required: true, submitOnChange: true
        }
        if (sFansCmd == "decr") {
            input "sFanDecr", "number", title: "Decrease the fan by this %", required: true, submitOnChange: true
        }
    }
}
        section (""){
            input "sLocks", "capability.lock", title: "...and these locks...", multiple: true, required: false, submitOnChange: true
            if (sLocks) {
                input "locksCmd", "enum", title: "...will...?", options:["lock":"Lock","unlock":"Unlock"], multiple: false, required: false, submitOnChange:true
            }
        }
        section ("") {
            input "sDoor", "capability.garageDoorControl", title: "...and these garage doors...", multiple: true, required: false, submitOnChange: true
            if (sDoor) {
                input "sDoorCmd", "enum", title: "...will...", options:["open":"Open","close":"Close"], multiple: false, required: false, submitOnChange:true
            }
        }
        section ("") {
            input "cTstat", "capability.thermostat", title: "...and these thermostats will...", multiple: true, required: false, submitOnChange:true
            if (cTstat) {
                input "cTstatFan", "enum", title: "...set the fan mode to...", options:["auto":"Auto","on":"On","off":"Off","circ":"Circulate"], multiple: false, required: false, submitOnChange:true
                input "cTstatMode", "enum", title: "...set the operating mode to...", options:["cool":"Cool","heat":"Heat","auto":"Auto","on":"On","off":"Off","incr":"Increase","decr":"Decrease"], multiple: false, required: false, submitOnChange:true
                if (cTstatMode in ["cool","auto"]) { input "coolLvl", "number", title: "Cool Setpoint", required: true, submitOnChange: true}
                if (cTstatMode in ["heat","auto"]) { input "heatLvl", "number", title: "Heat Setpoint", required: true, submitOnChange: true}
                if (cTstatMode in ["incr","decr"]) {
                    if (cTstatMode == "decr") {paragraph "NOTE: This will decrease the temp from the current room temp minus what you choose."}
                    if (cTstatMode == "incr") {paragraph "NOTE: This will increase the temp from the current room temp plus what you choose."}
                    input "tempChange", "number", title: "By this amount...", required: true, submitOnChange: true }
            }
        }
        if(cTstat) {
            section("") {
                input "cTstat1", "capability.thermostat", title: "More Thermostat(s)...", multiple: true, required: false, submitOnChange:true
                if (cTstat1) {
                    input "cTstat1Fan", "enum", title: "Fan Mode", options:["auto":"Auto","on":"On","off":"Off","circ":"Circulate"],multiple: false, required: false, submitOnChange:true
                    input "cTstat1Mode", "enum", title: "Operating Mode", options:["cool":"Cool","heat":"Heat","auto":"Auto","on":"On","off":"Off","incr":"Increase","decr":"Decrease"],multiple: false, required: false, submitOnChange:true
                    if (cTstat1Mode in ["cool","auto"]) { input "coolLvl1", "number", title: "Cool Setpoint", required: true, submitOnChange: true }
                    if (cTstat1Mode in ["heat","auto"]) { input "heatLvl1", "number", title: "Heat Setpoint", required: true, submitOnChange: true }
                    if (cTstat1Mode in ["incr","decr"]) {
                        if (cTstat1Mode == "decr") {paragraph "NOTE: This will decrease the temp from the current room temp minus what you choose."}
                        if (cTstat1Mode == "incr") {paragraph "NOTE: This will increase the temp from the current room temp plus what you choose."}
                        input "tempChange1", "number", title: "By this amount...", required: true, submitOnChange: true }
                }
            }
        }
        section (""){
            input "sVents", "capability.switchLevel", title: "...and these vents...", multiple: true, required: false, submitOnChange: true
            if (sVents) {
                input "ventsCmd", "enum", title: "...will...",
                    options:["on":"Open","off":"Close","25":"open to 25%","50":"open to 50%","75":"open to 75%"], multiple: false, required: false, submitOnChange:true
            }
        }
        section (""){
            input "sShades", "capability.windowShade", title: "...and these window coverings...", multiple: true, required: false, submitOnChange: true
            if (sShades) {
                input "shadesCmd", "enum", title: "...will...", options:["on":"Open","off":"Close","25":"open to 25%","50":"open to 50%","75":"open to 75%"], multiple: false, required: false,
                    submitOnChange:true
            }
        }
        section (""){
            input "scPresenceA", "capability.presenceSensor", title: "...and these presence sensors...", multiple: true, required: false, submitOnChange: true
            if (scPresenceA) {
                input "presenceCmdA", "enum", title: "...will become...", options:["present":"Present","departed":"Not Present"], multiple: false, required: false, submitOnChange:true
            }
        }
        section("") {  
            def modes = location.modes.name.sort()
            input "pMode", "enum", title: "...as well as changing the mode", options: modes, multiple: false, required: false
        }
    }
}

/******************************************************************************************************
RESTRICTIONS
******************************************************************************************************/
page name: "pRestrict"
def pRestrict(){
    dynamicPage(name: "pRestrict", title: "", uninstall: false) {
        section ("will be executed...") {
            input "modes", "mode", title: "...only when the mode is", multiple: true, required: false
        }        
        section (""){	
            input "days", title: "...or, only on these days of the week", multiple: true, required: false, submitOnChange: true,
                "enum", options: ["Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday", "Sunday"]
        }
        section (""){
            href "certainTime", title: "...or, only during this time schedule", description: pTimeComplete(), state: pTimeSettings()
        }
        section (""){
            input "rSwitch", "capability.switch", title: "...or, only if these switches are off", multiple: true, required: false, submitOnChange: true
            if (rSwitch?.size() > 1) {
                input "rSwitchAll", "bool", title: "Activate this toggle if you want ALL of the switches to be off", required: false, defaultValue: false, submitOnChange: true
            }
        }
    }
}

/************************************************************************************************************
Base Process
************************************************************************************************************/
def installed() {
    log.debug "Installed with settings: ${settings}, current app version: ${release()}"
    atomicState?.isInstalled = true
    initialize()
}

def updated() {
    log.debug "Updated with settings: ${settings}, current app version: ${release()}"
    unsubscribe()
    initialize()
}

def initialize() {
    if(scDim) {
        if(scDimCmd == "on") { subscribe(scDim, "switch.on", processActions) }
        if(scDimCmd == "off") { subscribe(scDim, "switch.off", processActions) }
        if(scDimCmd == "greater") { subscribe(scDim, "level", processDimTrigger) }
        if(scDimCmd == "lessThan") { subscribe(scDim, "level", processDimTrigger) }
        if(scDimCmd == "equal") { subscribe(scDim, "level", processDimTrigger) }
    }
    if(scTrigger) {
        if(scTriggerCmd == "on") { subscribe(scTrigger, "switch.on", processActions) }
        if(scTriggerCmd=="off") { subscribe(scTrigger, "switch.off", processActions) }
    }
    if(scTriggerCmd == on && ContactOff==true) { subscribe(scContact, "contact.closed", delay) }
    if(scTriggerCmd== off && ContactOn==true) { subscribe(scContact, "contact.open", delay) }
    if(scTriggerCmd == on && oContactOff==true) { subscribe(scContact, "contact.closed", delay) }
    if(scTriggerCmd== off && oContactOn==true) { subscribe(scContact, "contact.open", delay) }

    if(scMode) {
        subscribe (location, processModeChange) 
    }
    if (scContact) { 
        if (scContactS == "open") { subscribe(scContact, "contact.open", processActions) }
        if (scContactS == "closed") { subscribe(scContact, "contact.closed", processActions) }
    }
    if (scMotion) {
        log.info "motion trigger subscribed to"
        if (scMotionS == "active") { subscribe(scMotion, "motion.active", processActions) }
        if (scMotionS == "inactive") { subscribe(scMotion, "motion.inactive", processActions) }
    }    
    if (scPresenceT) {
        log.info "presence trigger subscribed to"
        if (scPresenceTA == "arrived") { subscribe(scPresenceT, "presence.present", processActions) }
        if (scPresenceTA == "departed") { subscribe(scPresenceT, "presence.not present", processActions) }
    }
    if (scLocks) {
        if (scLocksCmd == "locked") { subscribe(scLocks, "locks.lock", processActions) }
        if (scLocksCmd == "unlocked") { subscribe(scLocks, "locks.unlock", processActions) }
    }
}


def processModeChange(evt){
    def M = "${[evt.value]}"
    def S = "${scMode}"
    if (M == S) {
        processActions(evt)
    }
}  

/******************************************************************************
SHORTCUT HANDLERS
******************************************************************************/
def processDimTrigger(evt) {
    if (getDayOk()==true && getModeOk()==true && getTimeOk()==true && getSwitchOk()==true) {
        scDim.each {deviceD ->
            def currLevel = deviceD.latestValue("level")
            if (scDimCmd == "greater" && "${currLevel}" > "${scDimLvl}") { processActions() }
            if (scDimCmd == "lessThan" && "${currLevel}" < "${scDimLvl}") { processActions() }
            if (scDimCmd == "equal" && "${currLevel}" == "${scDimLvl}") { processActions() }
        }
    }
}

def delay(evt) {
    if (cOffDelay) { 
        if (parent.debug) log.debug "A $cOffDelay second delay has been called before $sSwitches can be changed"
        runIn(cOffDelay,contactCloses) 
    }
    else {
        contactCloses(evt)
    }
    if (ocOffDelay) { 
        if (parent.debug) log.debug "A $ocOffDelay second delay has been called before $sOtherSwitch can be changed"
        runIn(ocOffDelay,oContactCloses) 
    } 
    else {
        oContactCloses(evt)
    }
}

def contactCloses(evt){
    if (parent.debug) log.debug "The $cOffDelay second delay has elapsed, turning off $sSwitches"
    if (getDayOk()==true && getModeOk()==true && getTimeOk()==true && getSwitchOk()==true) {
        if (sSwitchesCmd=="on" && ContactOff==true) { sSwitches.off() }
        if (sSwitchesCmd=="off" && ContactOn==true) { sSwitches.on() }
    }
}

def oContactCloses(evt){
    if (parent.debug) log.debug "The $ocOffDelay second delay has elapsed, turning off $sOtherSwitch"
    if (getDayOk()==true && getModeOk()==true && getTimeOk()==true && getSwitchOk()==true) {
        if (sOtherSwitchCmd=="on" && oContactOff==true) { sOtherSwitch.off() }
        if (sOtherSwitchCmd=="off" && oContactOn==true) { sOtherSwitch.on() }
    }
}

def runShortcutAction(evt){
    if (getDayOk()==true && getModeOk()==true && getTimeOk()==true && getSwitchOk()==true) {
        if (sSwitches || sOtherSwitch) {
            if (sSwitchesCmd == "on") {sSwitches.on()}
            if (sSwitchesCmd == "off") {sSwitches.off()}
            if (sSwitchesCmd == "toggle") {toggle1()}
            if (sOtherSwitchCmd == "on") {sOtherSwitch.on()}
            if (sOtherSwitchCmd == "off") {sOtherSwitch.off()}
            if (sOtherSwitchCmd == "toggle") {toggle2()}
        }
        if (sHues) { processColor()}
        if (sDimmers) {
            if (sDimmersCmd == "on") {sDimmers.on()}
            else if (sDimmersCmd == "off") {sDimmers.off()}
            if (sDimmersCmd == "set" && sDimmers) {
                def level = sDimmersLVL < 0 || !sDimmersLVL ?  0 : sDimmersLVL >100 ? 100 : sDimmersLVL as int
                    sDimmers.setLevel(level)
            }
            if (sDimmersCmd == "increase" && sDimmers) {
                def newLevel
                sDimmers?.each {deviceD ->
                    def currLevel = deviceD.latestValue("level")
                    newLevel = sDimIncrease
                    newLevel = newLevel + currLevel
                    newLevel = newLevel < 0 ? 0 : newLevel >100 ? 100 : newLevel
                    deviceD.setLevel(newLevel)
                }
            }
            if (sDimmersCmd == "decrease" && sDimmers) {
                def newLevel
                sDimmers?.each {deviceD ->
                    def currLevel = deviceD.latestValue("level")
                    newLevel = sDimDecrease
                    newLevel = currLevel - newLevel
                    newLevel = newLevel < 0 ? 0 : newLevel >100 ? 100 : newLevel
                    deviceD.setLevel(newLevel)
                }
            }
        }
        if (sOtherDimmers) {
            if (sOtherDimmersCmd == "on") {sOtherDimmers.on()}
            else if (sOtherDimmersCmd == "off") {sOtherDimmers.off()}
            if (sOtherDimmersCmd == "set" && sOtherDimmers) {
                def otherLevel = sOtherDimmersLVL < 0 || !sOtherDimmersLVL ?  0 : sOtherDimmersLVL >100 ? 100 : sOtherDimmersLVL as int
                    sOtherDimmers?.setLevel(otherLevel)
            }
            if (sOtherDimmersCmd == "increase" && sOtherDimmers) {
                def newLevel
                sOtherDimmers?.each { deviceD ->
                    def currLevel = deviceD.latestValue("level")
                    newLevel = sOtherDimIncrease
                    newLevel = newLevel + currLevel
                    newLevel = newLevel < 0 ? 0 : newLevel >100 ? 100 : newLevel
                    deviceD.setLevel(newLevel)
                }
            }
            if (sOtherDimmersCmd == "decrease" && sOtherDimmers) {
                def newLevel
                sOtherDimmersCmd?.each { deviceD ->
                    def currLevel = deviceD.latestValue("level")
                    newLevel = sOtherDimDecrease
                    newLevel = currLevel - newLevel
                    newLevel = newLevel < 0 ? 0 : newLevel >100 ? 100 : newLevel
                    deviceD.setLevel(newLevel)
                }
            }
        }
        if (sDoor) {
            if ( sDoorCmd == "open") {
                sDoor.open() }
            else { sDoor.close() }
        }
        if (sLocks) {
            if (sLocksCmd == "lock") { sLocks.lock() }
            else { sLocks.unlock() }
        }
        if (mSwitchesOn) { momentaryDeviceHandlerOn() }
        if (mSwitchesOff) { momentaryDeviceHandlerOff() }
        if (scPresenceA) {
            if (presenceCmd == "arrived") { presence.arrived() }
            else if (presenceCmd == "departed") {presence.departed() }
        }
        if (sFans) {
            if (sFansCmd == "on") {sFans.on()}
            else if (sFansCmd == "off") {sFans.off()}
            else if (sFansCmd == "low") {sFans.setLevel(33)}
            else if (sFansCmd == "med") {sFans.setLevel(66)}
            else if (sFansCmd == "high") {sFans.setLevel(99)}
            if (sFansCmd == "incr" && sFans) {
                def newLevel
                sFans?.each {deviceD ->
                    def currLevel = deviceD.latestValue("level")
                    newLevel = sFanIncr
                    newLevel = newLevel + currLevel
                    newLevel = newLevel < 0 ? 0 : newLevel > 99 ? 99 : newLevel
                    deviceD.setLevel(newLevel)
                }
            }
            if (sFansCmd == "decr" && sFans) {
                def newLevel
                sFans?.each {deviceD ->
                    def currLevel = deviceD.latestValue("level")
                    newLevel = sFanDecr
                    newLevel = currLevel - newLevel
                    newLevel = newLevel < 0 ? 0 : newLevel > 99 ? 99 : newLevel
                    deviceD.setLevel(newLevel)
                }
            }
        }
        if (sVents) {
            if (sVentsCmd == "on") {sVents.setLevel(100)}
            else if (sVentsCmd == "off") {sVents.off()}
            else if (sVentsCmd == "25") {sVents.setLevel(25)}
            else if (sVentsCmd == "50") {sVents.setLevel(50)}
            else if (sVentsCmd == "75") {sVents.setLevel(75)}
        }
        if (sShades) {
            if (sShadesCmd == "open") {sShades.setLevel(100)}
            else if (sShadesCmd == "close") {sShades.setLevel(0)}
            else if (sShadesCmd == "25") {sShades.setLevel(25)}
            else if (sShadesCmd == "50") {sShades.setLevel(50)}
            else if (sShadesCmd == "75") {sShades.setLevel(75)}
        }
        if (cTstat) { thermostats() }
        if (cTstat1) { thermostats1() }
        if (pMode) { setLocationMode(pMode) }
        if (sFlash) { flashLights() }
    }
}


/************************************************************************************************************
THERMOSTATS HANDLERS
************************************************************************************************************/
private thermostats() {
    cTstat.each {deviceD ->
        def currentMode = deviceD.currentValue("thermostatMode")
        def currentTMP = deviceD.currentValue("temperature")
        if (cTstatMode == "off") { cTstat.off()
                                 }
        if (cTstatMode == "auto" || cTstatMode == "on") {
            cTstat.auto()
            cTstat.setCoolingSetpoint(coolLvl)
            cTstat.setHeatingSetpoint(heatLvl)
        }
        if (cTstatMode == "cool") {
            cTstat.cool()
            cTstat.setCoolingSetpoint(coolLvl)
        }
        if (cTstatMode == "heat") {
            cTstat.heat()
            cTstat.setHeatingSetpoint(heatLvl)
        }
        if (cTstatMode == "incr") {
            def cNewSetpoint = tempChange
            cNewSetpoint = tempChange + currentTMP
            cNewSetpoint = cNewSetpoint < 60 ? 60 : cNewSetpoint > 85 ? 85 : cNewSetpoint
            def hNewSetpoint = tempChange
            hNewSetpoint = tempChange + currentTMP
            hNewSetpoint = hNewSetpoint < 60 ? 60 : hNewSetpoint > 85 ? 85 : hNewSetpoint
            if (currentMode == "auto" || currentMode == "on") {
                deviceD.setCoolingSetpoint(cNewSetpoint)
                deviceD.setHeatingSetpoint(hNewSetPoint)
            }
            if (currentMode == "cool") {
                deviceD.setCoolingSetpoint(cNewSetpoint)
            }
            if (currentMode == "heat") {
                deviceD.setHeatingSetpoint(hNewSetPoint)
            }
        }
        if (cTstatMode == "decr") {
            def cNewSetpoint = tempChange
            cNewSetpoint = currentTMP - tempChange
            cNewSetpoint = cNewSetpoint < 60 ? 60 : cNewSetpoint > 85 ? 85 : cNewSetpoint
            def hNewSetpoint = tempChange
            hNewSetpoint = currentTMP - tempChange
            hNewSetpoint = hNewSetpoint < 60 ? 60 : hNewSetpoint > 85 ? 85 : hNewSetpoint
            if (currentMode == "auto" || currentMode == "on") {
                deviceD.setCoolingSetpoint(cNewSetpoint)
                deviceD.setHeatingSetpoint(hNewSetPoint)
            }
            if (currentMode == "cool") {
                deviceD.setCoolingSetpoint(cNewSetpoint)
            }
            if (currentMode == "heat") {
                deviceD.setHeatingSetpoint(hNewSetPoint)
            }
        }
        if (cTstatFan == "auto" || cTstatFan == "off") { cTstat.fanAuto() }
        if (cTstatFan == "on") { cTstat.fanOn() }
        if (cTstatFan == "circ") { cTstat.fanCirculate() }
    }
}

private thermostats1() {
    cTstat1.each {deviceD ->
        def currentMode = deviceD.currentValue("thermostatMode")
        def currentTMP = deviceD.currentValue("temperature")
        if (cTstat1Mode == "off") { cTstat1.off()
                                  }
        if (cTstat1Mode == "auto" || cTstat1Mode == "on") {
            cTstat1.auto()
            cTstat1.setCoolingSetpoint(coolLvl1)
            cTstat1.setHeatingSetpoint(heatLvl1)
        }
        if (cTstat1Mode == "auto" || cTstat1Mode == "on") {
            cTstat1.auto()
            cTstat1.setCoolingSetpoint(coolLvl1)
            cTstat1.setHeatingSetpoint(heatLvl1)
        }
        if (cTstat1Mode == "incr") {
            def cNewSetpoint = tempChange1
            cNewSetpoint = tempChange1 + currentTMP
            cNewSetpoint = cNewSetpoint < 60 ? 60 : cNewSetpoint > 85 ? 85 : cNewSetpoint
            def hNewSetpoint = tempChange1
            hNewSetpoint = tempChange1 + currentTMP
            hNewSetpoint = hNewSetpoint < 60 ? 60 : hNewSetpoint > 85 ? 85 : hNewSetpoint
            if (currentMode == "auto" || currentMode == "on") {
                deviceD.setCoolingSetpoint(cNewSetpoint)
                deviceD.setHeatingSetpoint(hNewSetPoint)
            }
            if (currentMode == "cool") {
                deviceD.setCoolingSetpoint(cNewSetpoint)
            }
            if (currentMode == "heat") {
                deviceD.setHeatingSetpoint(hNewSetPoint)
            }
        }
        if (cTsta1tMode == "decr") {
            def cNewSetpoint = tempChange1
            cNewSetpoint = currentTMP - tempChange1
            cNewSetpoint = cNewSetpoint < 60 ? 60 : cNewSetpoint > 85 ? 85 : cNewSetpoint
            def hNewSetpoint = tempChange1
            hNewSetpoint = currentTMP - tempChange1
            hNewSetpoint = hNewSetpoint < 60 ? 60 : hNewSetpoint > 85 ? 85 : hNewSetpoint
            if (currentMode == "auto" || currentMode == "on") {
                deviceD.setCoolingSetpoint(cNewSetpoint)
                deviceD.setHeatingSetpoint(hNewSetPoint)
            }
            if (currentMode == "cool") {
                deviceD.setCoolingSetpoint(cNewSetpoint)
            }
            if (currentMode == "heat") {
                deviceD.setHeatingSetpoint(hNewSetPoint)
            }
        }
        if (cTstat1Fan == "auto" || cTstat1Fan == "off") { cTstat1.fanAuto() }
        if (cTstat1Fan == "on") { cTstat1.fanOn() }
        if (cTstat1Fan == "circ") { cTstat1.fanCirculate() }
    }
}

/************************************************************************************************************
TOGGLE SWITCHES HANDLER
************************************************************************************************************/
private toggle1() {
    sSwitches.each { deviceName ->
        def switchattr = deviceName.currentSwitch
        if (switchattr.contains('on')) {
            deviceName.off()
        }
        else {
            deviceName.on()
        }
    }		
}

private toggle2() {
    sOtherSwitch.each { deviceName ->
        def switchattr = deviceName.currentSwitch
        if (switchattr.contains('on')) {
            deviceName.off()
        }
        else {
            deviceName.on()
        }
    }		
}

/************************************************************************************************************
Momentary Devices Handler On
************************************************************************************************************/
def momentaryDeviceHandlerOn() {
    runIn(mOn,"turnOn", [overwrite: true])
}
def turnOn() {
    mSwitchesOn.on()
}
  
/************************************************************************************************************
Momentary Devices Handler Off
************************************************************************************************************/
def momentaryDeviceHandlerOff() {
    runIn(mOff,"turnOff", [overwrite: true])
}
def turnOff() {
    mSwitchesOff.off()
}

/************************************************************************************************************
FLASHING LIGHTS HANDLER 
************************************************************************************************************/
private flashLights(evt) {
if (parent.debug) log.debug "The Flash Switches Option has been activated"
def doFlash = true
def onFor = onFor ?: 60000/60
def offFor = offFor ?: 60000/60
def numFlashes = numFlashes ?: 3

if (state.lastActivated) {
def elapsed = now() - state.lastActivated
def sequenceTime = (numFlashes + 1) * (onFor + offFor)
doFlash = elapsed > sequenceTime
}
if (doFlash) {
state.lastActivated = now()
def initialActionOn = sFlash.collect{it.currentflashSwitch != "on"}
def delay = 0L

numFlashes.times {
sFlash.eachWithIndex {s, i ->
if (initialActionOn[i]) {
s.on(delay: delay)
}
else {
s.off(delay:delay)
}
}
delay += onFor
sFlash.eachWithIndex {s, i ->
if (initialActionOn[i]) {
s.off(delay: delay)
}
else {
s.on(delay:delay)
}
}
delay += offFor
}
}
}

/******************************************************************************
COLOR BULBS HANDLER
******************************************************************************/
private processColor() {
    if (sHuesCmd == "on") { sHues?.on() }
    if (sHuesCmd == "off") { sHues?.off() }
    if (sHuesOtherCmd == "on") { sHuesOther?.on() }
    if (sHuesOtherCmd == "off") { sHuesOther?.off() }
    def hueSetVals = getColorName("${sHuesColor}",level)
    sHues?.setColor(hueSetVals)
    def    hueSetValsOther = getColorName("${sHuesOtherColor}",level)
    sHuesOther?.setColor(hueSetValsOther)
}

private getColorName(sHuesColor, level) {
    for (color in fillColorSettings()) {
        if (color.name.toLowerCase() == sHuesColor.toLowerCase()) {
            int hueVal = Math.round(color.h / 3.6)
            int hueLevel = !level ? color.l : level
            def hueSet = [hue: hueVal, saturation: color.s, level: hueLevel]
            return hueSet
        }
    }
}

def fillColorSettings() {
    return [
        [ name: "Soft White",                rgb: "#B6DA7C",        h: 83,        s: 44,        l: 67,    ],
        [ name: "Warm White",                rgb: "#DAF17E",        h: 51,        s: 20,        l: 100,    ],
        [ name: "Very Warm White",            rgb: "#DAF17E",        h: 51,        s: 60,        l: 51,    ],
        [ name: "Daylight White",            rgb: "#CEF4FD",        h: 191,        s: 9,        l: 90,    ],
        [ name: "Daylight",                    rgb: "#CEF4FD",        h: 191,        s: 9,        l: 90,    ],
        [ name: "Cool White",                rgb: "#F3F6F7",        h: 187,        s: 19,        l: 96,    ],
        [ name: "White",                    rgb: "#FFFFFF",        h: 0,        s: 0,        l: 100,    ],
        [ name: "Alice Blue",                rgb: "#F0F8FF",        h: 208,        s: 100,        l: 97,    ],
        [ name: "Antique White",            rgb: "#FAEBD7",        h: 34,        s: 78,        l: 91,    ],
        [ name: "Aqua",                        rgb: "#00FFFF",        h: 180,        s: 100,        l: 50,    ],
        [ name: "Aquamarine",                rgb: "#7FFFD4",        h: 160,        s: 100,        l: 75,    ],
        [ name: "Azure",                    rgb: "#F0FFFF",        h: 180,        s: 100,        l: 97,    ],
        [ name: "Beige",                    rgb: "#F5F5DC",        h: 60,        s: 56,        l: 91,    ],
        [ name: "Bisque",                    rgb: "#FFE4C4",        h: 33,        s: 100,        l: 88,    ],
        [ name: "Blanched Almond",            rgb: "#FFEBCD",        h: 36,        s: 100,        l: 90,    ],
        [ name: "Blue",                        rgb: "#0000FF",        h: 240,        s: 100,        l: 50,    ],
        [ name: "Blue Violet",                rgb: "#8A2BE2",        h: 271,        s: 76,        l: 53,    ],
        [ name: "Brown",                    rgb: "#A52A2A",        h: 0,        s: 59,        l: 41,    ],
        [ name: "Burly Wood",                rgb: "#DEB887",        h: 34,        s: 57,        l: 70,    ],
        [ name: "Cadet Blue",                rgb: "#5F9EA0",        h: 182,        s: 25,        l: 50,    ],
        [ name: "Chartreuse",                rgb: "#7FFF00",        h: 90,        s: 100,        l: 50,    ],
        [ name: "Chocolate",                rgb: "#D2691E",        h: 25,        s: 75,        l: 47,    ],
        [ name: "Coral",                    rgb: "#FF7F50",        h: 16,        s: 100,        l: 66,    ],
        [ name: "Corn Flower Blue",            rgb: "#6495ED",        h: 219,        s: 79,        l: 66,    ],
        [ name: "Corn Silk",                rgb: "#FFF8DC",        h: 48,        s: 100,        l: 93,    ],
        [ name: "Crimson",                    rgb: "#DC143C",        h: 348,        s: 83,        l: 58,    ],
        [ name: "Cyan",                        rgb: "#00FFFF",        h: 180,        s: 100,        l: 50,    ],
        [ name: "Dark Blue",                rgb: "#00008B",        h: 240,        s: 100,        l: 27,    ],
        [ name: "Dark Cyan",                rgb: "#008B8B",        h: 180,        s: 100,        l: 27,    ],
        [ name: "Dark Golden Rod",            rgb: "#B8860B",        h: 43,        s: 89,        l: 38,    ],
        [ name: "Dark Gray",                rgb: "#A9A9A9",        h: 0,        s: 0,        l: 66,    ],
        [ name: "Dark Green",                rgb: "#006400",        h: 120,        s: 100,        l: 20,    ],
        [ name: "Dark Khaki",                rgb: "#BDB76B",        h: 56,        s: 38,        l: 58,    ],
        [ name: "Dark Magenta",                rgb: "#8B008B",        h: 300,        s: 100,        l: 27,    ],
        [ name: "Dark Olive Green",            rgb: "#556B2F",        h: 82,        s: 39,        l: 30,    ],
        [ name: "Dark Orange",                rgb: "#FF8C00",        h: 33,        s: 100,        l: 50,    ],
        [ name: "Dark Orchid",                rgb: "#9932CC",        h: 280,        s: 61,        l: 50,    ],
        [ name: "Dark Red",                    rgb: "#8B0000",        h: 0,        s: 100,        l: 27,    ],
        [ name: "Dark Salmon",                rgb: "#E9967A",        h: 15,        s: 72,        l: 70,    ],
        [ name: "Dark Sea Green",            rgb: "#8FBC8F",        h: 120,        s: 25,        l: 65,    ],
        [ name: "Dark Slate Blue",            rgb: "#483D8B",        h: 248,        s: 39,        l: 39,    ],
        [ name: "Dark Slate Gray",            rgb: "#2F4F4F",        h: 180,        s: 25,        l: 25,    ],
        [ name: "Dark Turquoise",            rgb: "#00CED1",        h: 181,        s: 100,        l: 41,    ],
        [ name: "Dark Violet",                rgb: "#9400D3",        h: 282,        s: 100,        l: 41,    ],
        [ name: "Deep Pink",                rgb: "#FF1493",        h: 328,        s: 100,        l: 54,    ],
        [ name: "Deep Sky Blue",            rgb: "#00BFFF",        h: 195,        s: 100,        l: 50,    ],
        [ name: "Dim Gray",                    rgb: "#696969",        h: 0,        s: 0,        l: 41,    ],
        [ name: "Dodger Blue",                rgb: "#1E90FF",        h: 210,        s: 100,        l: 56,    ],
        [ name: "Fire Brick",                rgb: "#B22222",        h: 0,        s: 68,        l: 42,    ],
        [ name: "Floral White",                rgb: "#FFFAF0",        h: 40,        s: 100,        l: 97,    ],
        [ name: "Forest Green",                rgb: "#228B22",        h: 120,        s: 61,        l: 34,    ],
        [ name: "Fuchsia",                    rgb: "#FF00FF",        h: 300,        s: 100,        l: 50,    ],
        [ name: "Gainsboro",                rgb: "#DCDCDC",        h: 0,        s: 0,        l: 86,    ],
        [ name: "Ghost White",                rgb: "#F8F8FF",        h: 240,        s: 100,        l: 99,    ],
        [ name: "Gold",                        rgb: "#FFD700",        h: 51,        s: 100,        l: 50,    ],
        [ name: "Golden Rod",                rgb: "#DAA520",        h: 43,        s: 74,        l: 49,    ],
        [ name: "Gray",                        rgb: "#808080",        h: 0,        s: 0,        l: 50,    ],
        [ name: "Green",                    rgb: "#008000",        h: 120,        s: 100,        l: 25,    ],
        [ name: "Green Yellow",                rgb: "#ADFF2F",        h: 84,        s: 100,        l: 59,    ],
        [ name: "Honeydew",                    rgb: "#F0FFF0",        h: 120,        s: 100,        l: 97,    ],
        [ name: "Hot Pink",                    rgb: "#FF69B4",        h: 330,        s: 100,        l: 71,    ],
        [ name: "Indian Red",                rgb: "#CD5C5C",        h: 0,        s: 53,        l: 58,    ],
        [ name: "Indigo",                    rgb: "#4B0082",        h: 275,        s: 100,        l: 25,    ],
        [ name: "Ivory",                    rgb: "#FFFFF0",        h: 60,        s: 100,        l: 97,    ],
        [ name: "Khaki",                    rgb: "#F0E68C",        h: 54,        s: 77,        l: 75,    ],
        [ name: "Lavender",                    rgb: "#E6E6FA",        h: 240,        s: 67,        l: 94,    ],
        [ name: "Lavender Blush",            rgb: "#FFF0F5",        h: 340,        s: 100,        l: 97,    ],
        [ name: "Lawn Green",                rgb: "#7CFC00",        h: 90,        s: 100,        l: 49,    ],
        [ name: "Lemon Chiffon",            rgb: "#FFFACD",        h: 54,        s: 100,        l: 90,    ],
        [ name: "Light Blue",                rgb: "#ADD8E6",        h: 195,        s: 53,        l: 79,    ],
        [ name: "Light Coral",                rgb: "#F08080",        h: 0,        s: 79,        l: 72,    ],
        [ name: "Light Cyan",                rgb: "#E0FFFF",        h: 180,        s: 100,        l: 94,    ],
        [ name: "Light Golden Rod Yellow",    rgb: "#FAFAD2",        h: 60,        s: 80,        l: 90,    ],
        [ name: "Light Gray",                rgb: "#D3D3D3",        h: 0,        s: 0,        l: 83,    ],
        [ name: "Light Green",                rgb: "#90EE90",        h: 120,        s: 73,        l: 75,    ],
        [ name: "Light Pink",                rgb: "#FFB6C1",        h: 351,        s: 100,        l: 86,    ],
        [ name: "Light Salmon",                rgb: "#FFA07A",        h: 17,        s: 100,        l: 74,    ],
        [ name: "Light Sea Green",            rgb: "#20B2AA",        h: 177,        s: 70,        l: 41,    ],
        [ name: "Light Sky Blue",            rgb: "#87CEFA",        h: 203,        s: 92,        l: 75,    ],
        [ name: "Light Slate Gray",            rgb: "#778899",        h: 210,        s: 14,        l: 53,    ],
        [ name: "Light Steel Blue",            rgb: "#B0C4DE",        h: 214,        s: 41,        l: 78,    ],
        [ name: "Light Yellow",                rgb: "#FFFFE0",        h: 60,        s: 100,        l: 94,    ],
        [ name: "Lime",                        rgb: "#00FF00",        h: 120,        s: 100,        l: 50,    ],
        [ name: "Lime Green",                rgb: "#32CD32",        h: 120,        s: 61,        l: 50,    ],
        [ name: "Linen",                    rgb: "#FAF0E6",        h: 30,        s: 67,        l: 94,    ],
        [ name: "Maroon",                    rgb: "#800000",        h: 0,        s: 100,        l: 25,    ],
        [ name: "Medium Aquamarine",        rgb: "#66CDAA",        h: 160,        s: 51,        l: 60,    ],
        [ name: "Medium Blue",                rgb: "#0000CD",        h: 240,        s: 100,        l: 40,    ],
        [ name: "Medium Orchid",            rgb: "#BA55D3",        h: 288,        s: 59,        l: 58,    ],
        [ name: "Medium Purple",            rgb: "#9370DB",        h: 260,        s: 60,        l: 65,    ],
        [ name: "Medium Sea Green",            rgb: "#3CB371",        h: 147,        s: 50,        l: 47,    ],
        [ name: "Medium Slate Blue",        rgb: "#7B68EE",        h: 249,        s: 80,        l: 67,    ],
        [ name: "Medium Spring Green",        rgb: "#00FA9A",        h: 157,        s: 100,        l: 49,    ],
        [ name: "Medium Turquoise",            rgb: "#48D1CC",        h: 178,        s: 60,        l: 55,    ],
        [ name: "Medium Violet Red",        rgb: "#C71585",        h: 322,        s: 81,        l: 43,    ],
        [ name: "Midnight Blue",            rgb: "#191970",        h: 240,        s: 64,        l: 27,    ],
        [ name: "Mint Cream",                rgb: "#F5FFFA",        h: 150,        s: 100,        l: 98,    ],
        [ name: "Misty Rose",                rgb: "#FFE4E1",        h: 6,        s: 100,        l: 94,    ],
        [ name: "Moccasin",                    rgb: "#FFE4B5",        h: 38,        s: 100,        l: 85,    ],
        [ name: "Navajo White",                rgb: "#FFDEAD",        h: 36,        s: 100,        l: 84,    ],
        [ name: "Navy",                        rgb: "#000080",        h: 240,        s: 100,        l: 25,    ],
        [ name: "Old Lace",                    rgb: "#FDF5E6",        h: 39,        s: 85,        l: 95,    ],
        [ name: "Olive",                    rgb: "#808000",        h: 60,        s: 100,        l: 25,    ],
        [ name: "Olive Drab",                rgb: "#6B8E23",        h: 80,        s: 60,        l: 35,    ],
        [ name: "Orange",                    rgb: "#FFA500",        h: 39,        s: 100,        l: 50,    ],
        [ name: "Orange Red",                rgb: "#FF4500",        h: 16,        s: 100,        l: 50,    ],
        [ name: "Orchid",                    rgb: "#DA70D6",        h: 302,        s: 59,        l: 65,    ],
        [ name: "Pale Golden Rod",            rgb: "#EEE8AA",        h: 55,        s: 67,        l: 80,    ],
        [ name: "Pale Green",                rgb: "#98FB98",        h: 120,        s: 93,        l: 79,    ],
        [ name: "Pale Turquoise",            rgb: "#AFEEEE",        h: 180,        s: 65,        l: 81,    ],
        [ name: "Pale Violet Red",            rgb: "#DB7093",        h: 340,        s: 60,        l: 65,    ],
        [ name: "Papaya Whip",                rgb: "#FFEFD5",        h: 37,        s: 100,        l: 92,    ],
        [ name: "Peach Puff",                rgb: "#FFDAB9",        h: 28,        s: 100,        l: 86,    ],
        [ name: "Peru",                        rgb: "#CD853F",        h: 30,        s: 59,        l: 53,    ],
        [ name: "Pink",                        rgb: "#FFC0CB",        h: 350,        s: 100,        l: 88,    ],
        [ name: "Plum",                        rgb: "#DDA0DD",        h: 300,        s: 47,        l: 75,    ],
        [ name: "Powder Blue",                rgb: "#B0E0E6",        h: 187,        s: 52,        l: 80,    ],
        [ name: "Purple",                    rgb: "#800080",        h: 300,        s: 100,        l: 25,    ],
        [ name: "Red",                        rgb: "#FF0000",        h: 0,        s: 100,        l: 50,    ],
        [ name: "Rosy Brown",                rgb: "#BC8F8F",        h: 0,        s: 25,        l: 65,    ],
        [ name: "Royal Blue",                rgb: "#4169E1",        h: 225,        s: 73,        l: 57,    ],
        [ name: "Saddle Brown",                rgb: "#8B4513",        h: 25,        s: 76,        l: 31,    ],
        [ name: "Salmon",                    rgb: "#FA8072",        h: 6,        s: 93,        l: 71,    ],
        [ name: "Sandy Brown",                rgb: "#F4A460",        h: 28,        s: 87,        l: 67,    ],
        [ name: "Sea Green",                rgb: "#2E8B57",        h: 146,        s: 50,        l: 36,    ],
        [ name: "Sea Shell",                rgb: "#FFF5EE",        h: 25,        s: 100,        l: 97,    ],
        [ name: "Sienna",                    rgb: "#A0522D",        h: 19,        s: 56,        l: 40,    ],
        [ name: "Silver",                    rgb: "#C0C0C0",        h: 0,        s: 0,        l: 75,    ],
        [ name: "Sky Blue",                    rgb: "#87CEEB",        h: 197,        s: 71,        l: 73,    ],
        [ name: "Slate Blue",                rgb: "#6A5ACD",        h: 248,        s: 53,        l: 58,    ],
        [ name: "Slate Gray",                rgb: "#708090",        h: 210,        s: 13,        l: 50,    ],
        [ name: "Snow",                        rgb: "#FFFAFA",        h: 0,        s: 100,        l: 99,    ],
        [ name: "Spring Green",                rgb: "#00FF7F",        h: 150,        s: 100,        l: 50,    ],
        [ name: "Steel Blue",                rgb: "#4682B4",        h: 207,        s: 44,        l: 49,    ],
        [ name: "Tan",                        rgb: "#D2B48C",        h: 34,        s: 44,        l: 69,    ],
        [ name: "Teal",                        rgb: "#008080",        h: 180,        s: 100,        l: 25,    ],
        [ name: "Thistle",                    rgb: "#D8BFD8",        h: 300,        s: 24,        l: 80,    ],
        [ name: "Tomato",                    rgb: "#FF6347",        h: 9,        s: 100,        l: 64,    ],
        [ name: "Turquoise",                rgb: "#40E0D0",        h: 174,        s: 72,        l: 56,    ],
        [ name: "Violet",                    rgb: "#EE82EE",        h: 300,        s: 76,        l: 72,    ],
        [ name: "Wheat",                    rgb: "#F5DEB3",        h: 39,        s: 77,        l: 83,    ],
        [ name: "White Smoke",                rgb: "#F5F5F5",        h: 0,        s: 0,        l: 96,    ],
        [ name: "Yellow",                    rgb: "#FFFF00",        h: 60,        s: 100,        l: 50,    ],
        [ name: "Yellow Green",                rgb: "#9ACD32",        h: 80,        s: 61,        l: 50,    ],
    ]
}

/******************************************************************************************************
RESTRICTIONS
******************************************************************************************************/
page name: "certainTime"
def certainTime() {
    dynamicPage(name:"certainTime",title: "", uninstall: false) {
        section("") {
            input "startingX", "enum", title: "Starting at...", options: ["A specific time", "Sunrise", "Sunset"], required: false , submitOnChange: true
            if(startingX in [null, "A specific time"]) input "starting", "time", title: "Start time", required: false, submitOnChange: true
            else {
                if(startingX == "Sunrise") input "startSunriseOffset", "number", range: "*..*", title: "Offset in minutes (+/-)", required: false, submitOnChange: true
                else if(startingX == "Sunset") input "startSunsetOffset", "number", range: "*..*", title: "Offset in minutes (+/-)", required: false, submitOnChange: true
                    }
        }
        section("") {
            input "endingX", "enum", title: "Ending at...", options: ["A specific time", "Sunrise", "Sunset"], required: false, submitOnChange: true
            if(endingX in [null, "A specific time"]) input "ending", "time", title: "End time", required: false, submitOnChange: true
            else {
                if(endingX == "Sunrise") input "endSunriseOffset", "number", range: "*..*", title: "Offset in minutes (+/-)", required: false, submitOnChange: true
                else if(endingX == "Sunset") input "endSunsetOffset", "number", range: "*..*", title: "Offset in minutes (+/-)", required: false, submitOnChange: true
                    }
        }
    }
}

/***********************************************************************************************************************
RESTRICTIONS HANDLER
***********************************************************************************************************************/
private getSwitchOk() {
    def result = true
    def devList = []
    if(rSwitchAll) {
        if (rSwitch) {
            rSwitch.each { deviceName ->
                if (deviceName.latestValue("switch")== "on"){
                    String device  = (String) deviceName
                    devList += device
                }
            }
            if (devList?.size() > 1) result = false
        }   
        if (parent.debug) log.warn "switchOk = $result"
        if (rSwitch) log.warn "All devices required to be active & there are ${devList?.size()} devices active for restriction devices: $rSwitch"
        result
    }
    else if(!rSwitchAll) {
        if (rSwitch) {
            rSwitch.each { deviceName ->
                if (deviceName.latestValue("switch")== "on"){
                    String device  = (String) deviceName
                    devList += device
                }
            }
            if (devList?.size() > 0) result = false
        }   
        if (parent.debug) log.warn "switchOk = $result"
        if (rSwitch) log.warn "There are ${devList?.size()} devices active for restriction devices: $rSwitch"
        result
    }
}
  
private getModeOk() {
    def result = !modes || modes?.contains(location.mode)
    if (parent.debug) log.warn "modeOk = $result"
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
    if(parent.debug) log.warn "daysOk = $result"
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
            }
    if(parent.debug) log.warn "timeOk = $result"
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
    def result = "complete"
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

/******************************************************************************************************
PARENT STATUS CHECKS
******************************************************************************************************/
def checkRelease() {
    return state.release
}

// TRIGGER ACTIONS
def pTriggerSettings() {
    if (scTrigger || scMode || scContactS || scMotion || scPresenceT || scDimCmd) {
        return "complete"
    }
    return ""
}

def pTriggerComplete() {
    if (scTrigger || scMode || scContactS || scMotion || scPresenceT || scDimCmd) {
        return "Triggers have been Selected!"
    }
    return "Tap here to select devices"
}  

// PROFILE ACTIONS
def pDevicesSettings() {
    if(sDoor || cTstat || cTstat1 || sVents || sShades || presence || sSwitches || sOtherSwitch || sDimmers || sOtherDimmers || sHues || sHuesOther || mSwitchesOn || mSwitchesOff || sFans || sLocks) { 
        return "complete"
    }
    return ""
}

def pDevicesComplete() {
    if (sDoor || cTstat || cTstat1 || sVents || sShades || presence || sSwitches || sOtherSwitch || sDimmers || sOtherDimmers || sHues || sHuesOther || mSwitchesOn || mSwitchesOff || sFans || sLocks) {
        return "Actions have been Configured!"
    }
    return "Tap here to select devices"
}

// RESTRICTIONS
def pRestrictSettings(){ def result = "" 
                        if (modes || runDay || startingX || endingX) { 
                            result = "complete"}
                        result}
def pRestrictComplete() {def text = "Tap here to configure" 
                         if (modes || runDay || startingX || endingX) {
                             text = "Configured"}
                         else text = "Tap here to Configure"
                         text}
def pTimeSettings(){ def result = "" 
                    if (startingX || endingX) { 
                        result = "complete"}
                    result}
def pTimeComplete() {def text = "Tap here to configure" 
                     if (startingX || endingX) {
                         text = "Configured"}
                     else text = "Tap here to Configure"
                     text}