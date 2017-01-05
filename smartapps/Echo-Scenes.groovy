/**
 *  Scenes
 *
 *  Copyright 2017 Bobby Dobrescu
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
 */
 
definition(
    name: "Scenes",
    namespace: "bd",
    author: "Bobby Dobrescu",
    description: "Create scenes for a Profile that can be controlled by Echosistant.",
    category: "Convenience",
	parent: "EchoLabs:EchoSistantLabs",
    iconUrl			: "https://raw.githubusercontent.com/BamaRayne/Echosistant/master/smartapps/bamarayne/echosistant.src/app-Echosistant.png",
	iconX2Url		: "https://raw.githubusercontent.com/BamaRayne/Echosistant/master/smartapps/bamarayne/echosistant.src/app-Echosistant@2x.png",
	iconX3Url		: "https://raw.githubusercontent.com/BamaRayne/Echosistant/master/smartapps/bamarayne/echosistant.src/app-Echosistant@2x.png")

preferences {
    page name:"sceneSetup"
}

page name: "sceneSetup"
def sceneSetup() {
            dynamicPage(name: "sceneSetup", title: "",install: true, uninstall: true,submitOnChange: true) {
                 section ("Light Switches", hideWhenEmpty: true){
                    input "sSwitches", "capability.switch", title: "Select Lights and Switches...", multiple: true, required: false, submitOnChange: true
                        if (sSwitches) input "sSwitchCmd", "enum", title: "What do you want to do with these switches?", 
                        					options:["on":"Turn on","off":"Turn off","toggle":"Toggle"], multiple: false, required: false, submitOnChange:true
                        if (sSwitchCmd)	input "delaySwitches", "bool", title: "Delay Actions?", required: false, defaultValue: false, submitOnChange:true
                        	if (delaySwitches) {
                        		input "sSecondsOn", "number", title: "Turn on in Seconds?", defaultValue: none, required: false
                        		input "sSecondsOff", "number", title: "Turn off in Seconds?", defaultValue: none, required: false
                            }
                        if (sSwitchCmd) input "sOtherSwitch", "capability.switch", title: "...and these other switches?", multiple: true, required: false, submitOnChange: true                        
                        if (sOtherSwitch) input "sOtherSwitchCmd", "enum", title: "What do you want to do with these other switches?", 
                        					options: ["on1":"Turn on","off1":"Turn off","toggle1":"Toggle"], multiple: false, required: false, submitOnChange: true
                        if (sOtherSwitchCmd)	input "delayOtherSwitches", "bool", title: "Delay Actions?", required: false, defaultValue: false, submitOnChange:true
                        	if (delayOtherSwitches) {
                        		input "sOtherSecondsOn", "number", title: "Turn on in Seconds?", defaultValue: none, required: false
                        		input "sOtherSecondsOff", "number", title: "Turn off in Seconds?", defaultValue: none, required: false
                            }
                }
                section ("Dimmers", hideWhenEmpty: true){
                    input "sDimmers", "capability.switchLevel", title: "Select Dimmers...", multiple: true, required: false , submitOnChange:true
                        if (sDimmers) input "sDimmersCmd", "enum", title: "Command To Send To Dimmers", 
                        					options:["on":"Turn on","off":"Turn off", "set":"Set level"], multiple: false, required: false, submitOnChange:true
                        if (sDimmersCmd) input "sDimmersLVL", "number", title: "Dimmers Level", description: "Set dimmer level", required: false, submitOnChange: true	
                        if (sDimmersCmd) input "delayDimmers", "bool", title: "Delay Actions?", required: false, defaultValue: false, submitOnChange:true
							if (delayDimmers) {
                        		input "sSecondsDimmers", "number", title: "Turn on in Seconds?", defaultValue: none, required: false
                        		input "sSecondsDimmersOff", "number", title: "Turn off in Seconds?", defaultValue: none, required: false                        
                        	}
                        if (sDimmersCmd) input "sOtherDimmers", "capability.switchLevel", title: "...and these other Dimmers...", multiple: true, required: false , submitOnChange:true
                        if (sOtherDimmers) input "sOtherDimmersCmd", "enum", title: "Command To Send To Other Dimmers", 
                        					options:["on":"Turn on","off":"Turn off","set":"Set level"], multiple: false, required: false, submitOnChange:true
                        if (sOtherDimmersCmd) input "sOtherDimmersLVL", "number", title: "Dimmers Level", description: "Set dimmer level", required: false, submitOnChange: true
                        if (sOtherDimmersCmd)	input "delayOtherDimmers", "bool", title: "Delay Actions?", required: false, defaultValue: false, submitOnChange: true
							if (delayOtherDimmers) {
                        		input "sSecondsOtherDimmers", "number", title: "Turn on in Seconds?", defaultValue: none, required: false
                        		input "sSecondsOtherDimmersOff", "number", title: "Turn off in Seconds?", defaultValue: none, required: false                        
                        	}                
                }
				section ("Colored lights", hideWhenEmpty: true){
            		input "sHues", "capability.colorControl", title: "Select These Colored Lights...", multiple: true, required: false, submitOnChange:true
            			if (sHues) {
                        	input "sHuesCmd", "enum", title: "What do you want to do with these color bulbs?", options:["on":"Turn on","off":"Turn off","setColor":"Set Color"], multiple: false, required: false, submitOnChange:true
							input "sHuesColor", "enum", title: "Hue Color?", required: false, multiple:false, options: fillColorSettings().name
							input "sHuesLevel", "enum", title: "Light Level?", required: false, options: [[10:"10%"],[20:"20%"],[30:"30%"],[40:"40%"],[50:"50%"],[60:"60%"],[70:"70%"],[80:"80%"],[90:"90%"],[100:"100%"]], submitOnChange:true                        
        					}
                        if (sHuesLevel)	input "sHuesOther", "capability.colorControl", title: "...and these other Colored Lights?", multiple: true, required: false, submitOnChange:true
            			if (sHuesOther) {
                        	input "sHuesOtherCmd", "enum", title: "What do you want to do with these other color bulbs?", options:["on":"Turn on","off":"Turn off","setColor":"Set Color"], multiple: false, required: false, submitOnChange:true
							input "sHuesOtherColor", "enum", title: "Hue Color?", required: false, multiple:false, options: fillColorSettings().name
							input "sHuesOtherLevel", "enum", title: "Light Level?", required: false, options: [[10:"10%"],[20:"20%"],[30:"30%"],[40:"40%"],[50:"50%"],[60:"60%"],[70:"70%"],[80:"80%"],[90:"90%"],[100:"100%"]]                       
        					}
				}
                section ("Flash These Switches") {
					input "sFlash", "capability.switch", title: "Select Flashers", multiple: true, required: false, submitOnChange:true
					if (sFlash) {
                    	input "numFlashes", "number", title: "This number of times (default 3)", required: false, submitOnChange:true
                    	input "onFor", "number", title: "On for (default 1 second)", required: false, submitOnChange:true			
                            if (onFor) {
                                input "sFlashColorA", "enum", title: "Flasher Color?", required: false, multiple:false, options: fillColorSettings().name
                                input "sFlashLevelA", "enum", title: "Light Flash Level?", required: false, options: [[10:"10%"],[20:"20%"],[30:"30%"],[40:"40%"],[50:"50%"],[60:"60%"],[70:"70%"],[80:"80%"],[90:"90%"],[100:"100%"]]                       
                            }
                    	input "offFor", "number", title: "Off for (default 1 second)", required: false, submitOnChange:true
                            if (offFor) {
                                input "sFlashColorB", "enum", title: "Flasher Second Color?", required: false, multiple:false, options: fillColorSettings().name
                                input "sFlashLevelB", "enum", title: "Light Flash Level?", required: false, options: [[10:"10%"],[20:"20%"],[30:"30%"],[40:"40%"],[50:"50%"],[60:"60%"],[70:"70%"],[80:"80%"],[90:"90%"],[100:"100%"]] 					
                            }
                	}
                }
                section ("Mode", hideWhenEmpty: true){
                	input "sMode", "enum", title: "Change Location Mode to...", options: location.modes.name.sort(), multiple: false, required: false, submitOnChange: true
                }
                section ("Routine", hideWhenEmpty: true){
                	def actions = location.helloHome?.getPhrases()*.label 
                		if (actions) {
                    		actions.sort()
            			}                
                	input "runRoutine", "enum", title: "Select a Routine(s) to execute", required: false, options: actions, multiple: true
                }
                section([title:"Options", mobileOnly:true]) {
            			label title:"Name your Scene", required:false, defaultValue: "New Scene"
				}

         }
    }

def installed() {
    initialize()
}

def updated() {
    unschedule()
    initialize()
}

def initialize() {
app.updateLabel(sceneName)
}


def onEventA() {
	def A_levelOn = sDimmersLVL as Integer
        if (sSwitches) {
            settings.sDimmers?.setLevel(A_levelOn)
            settings.sSwitches?.on()
		}
        else {
    		if (settings.A_turnOff) {
				runIn(A_turnOff * 60, "delayTurnOffA")
        		state.A_timerStart = true
        	}
        	else {
        	settings.sSwitches?.off()
			settings.sDimmers?.setLevel(0)
        	}
		}
}

/***********************************************************************************************************************
    LAST MESSAGE HANDLER - PROFILE
***********************************************************************************************************************/
def runActionsHandler() {
 				if (hues) {               
                colorB() 
               	}
                if (flashSwitches) {
                flashLights()
                }
                profileDeviceControl()
                if (runRoutine) {
                location.helloHome?.execute(settings.runRoutine)
                }
				if (newMode) {
				setLocationMode(newMode)
                log.info "The mode has been changed from '${location.mode}' to '${setMode}'"
				}
}


/************************************************************************************************************
   Switch/Color/Dimmer/Toggle Handlers
************************************************************************************************************/
// Used for delayed devices
def turnOnSwitch() { switches?.on() }  
def turnOffSwitch() { switches?.off() }
def turnOnOtherSwitch() { otherSwitch?.on() }
def turnOffOtherSwitch() { otherSwitch?.off() }  
def turnOnDimmers() { def level = dimmersLVL < 0 || !dimmersLVL ?  0 : dimmersLVL >100 ? 100 : dimmersLVL as int
			dimmers?.setLevel(dimmersLVL) }
def turnOffDimmers() { dimmers?.off() }
def turnOnOtherDimmers() { def otherlevel = otherDimmersLVL < 0 || !otherDimmersLVL ?  0 : otherDimmersLVL >100 ? 100 : otherDimmersLVL as int
			otherDimmers?.setLevel(otherDimmersLVL) }
def turnOffOtherDimmers() { otherDimmers?.off() }            

// Primary control of profile triggered lights/switches when delayed
def profileDeviceControl() {
	if (sSecondsOn) { runIn(sSecondsOn,turnOnSwitch)}
    if (sSecondsOff) { runIn(sSecondsOff,turnOffSwitch)}
    if (sSecondsOn1)  { runIn(sSecondsOn1,turnOnOtherSwitch)}
    if (sSecondsOff1) { runIn(sSecondsOff1,turnOffOtherSwitch)}
	if (sSecondsOn2) { runIn(sSecondsOn2,turnOnDimmers)}
	if (sSecondsOff2) { runIn(sSecondsOff2,turnOffDimmers)}
    if (sSecondsOn3) { runIn(sSecondsOn3,turnOnOtherDimmers)}
	if (sSecondsOff3) { runIn(sSecondsOff3,turnOffOtherDimmers)}

// Control of Lights and Switches when not delayed            
    if (!sSecondsOn) {
            	if  (switchCmd == "on") { switches?.on() }
	            	else if (switchCmd == "off") { switches?.off() }
	           		if (switchCmd == "toggle") { toggle() }
                if (otherSwitchCmd == "on") { otherSwitch?.on() }
            		else if (otherSwitchCmd == "off") { otherSwitch?.off() }
                    if (otherSwitchCmd == "toggle") { toggle() }
                if (dimmersCmd == "set" && dimmers) { def level = dimmersLVL < 0 || !dimmersLVL ?  0 : dimmersLVL >100 ? 100 : dimmersLVL as int
        				dimmers?.setLevel(level) }
            	if (otherDimmersCmd == "set" && otherDimmers) { def otherlevel = otherDimmersLVL < 0 || !otherDimmersLVL ?  0 : otherDimmersLVL >100 ? 100 : otherDimmersLVL as int
        				otherDimmers?.setLevel(otherlevel) }
                }
			}

private colorB() { 
	if (hueCmd == "off") { hues?.off() }
    if (hueCmd1 == "off") { hues1?.off() }
		if (debug) log.debug "color bulbs initiated"
		def hueColor = 0
        def hueColor1 = 0
        fillColorSettings()
        if (color == "White")hueColor = 48
        if (color == "Red")hueColor = 0
        if (color == "Blue")hueColor = 70//60  
        if (color == "Green")hueColor = 39//30
        
        if(color == "Yellow")hueColor = 25//16
        if(color == "Orange")hueColor = 11
        if(color == "Purple")hueColor = 75
        if(color == "Pink")hueColor = 83
        
        
        
	def colorB = [hue: hueColor, hue1: hueColor1, saturation: 100, level: (lightLevel as Integer) ?: 100]
    hues*.setColor(colorB)
	}
 
 
 def fillColorSettings(){
	def colorData = []
    colorData << [name: "White", hue: 0, sat: 0] << [name: "Orange", hue: 11, sat: 100] << [name: "Red", hue: 100, sat: 100] << [name: "Purple", hue: 77, sat: 100]
    colorData << [name: "Green", hue: 30, sat: 100] << [name: "Blue", hue: 66, sat: 100] << [name: "Yellow", hue: 16, sat: 100] << [name: "Pink", hue: 95, sat: 100]
    colorData << [name: "Cyan", hue: 50, sat: 100] << [name: "Chartreuse", hue: 25, sat: 100] << [name: "Teal", hue: 44, sat: 100] << [name: "Magenta", hue: 92, sat: 100]
	colorData << [name: "Violet", hue: 83, sat: 100] << [name: "Indigo", hue: 70, sat: 100]<< [name: "Marigold", hue: 16, sat: 75]<< [name: "Raspberry", hue: 99, sat: 75]
    colorData << [name: "Fuchsia", hue: 92, sat: 75] << [name: "Lavender", hue: 83, sat: 75]<< [name: "Aqua", hue: 44, sat: 75]<< [name: "Amber", hue: 11, sat: 75]
    colorData << [name: "Carnation", hue: 99, sat: 50] << [name: "Periwinkle", hue: 70, sat: 50]<< [name: "Pistachio", hue: 30, sat: 50]<< [name: "Vanilla", hue: 16, sat: 50]
    if (customName && (customHue > -1 && customerHue < 101) && (customSat > -1 && customerSat < 101)) colorData << [name: customName, hue: customHue as int, sat: customSat as int]
    return colorData    
}    
    
private toggle() {
    if (parent.debug) log.debug "The selected device is toggling now"
	if (switches) {
	if (switches?.currentValue('switch').contains('on')) {
		switches?.off()
		}
    else if (switches?.currentValue('switch').contains('off')) {
		switches?.on()
		}
    }
    if (otherSwitch) {
	if (otherSwitch?.currentValue('switch').contains('on')) {
		otherSwitch?.off()
	}
	else if (otherSwitch?.currentValue('switch').contains('off')) {
		otherSwitch?.on()
		}
	}
	if (lock) {
	if (lock?.currentValue('lock').contains('locked')) {
		lock?.unlock()
		}
    }
	if (parent.debug) log.debug "The selected device has toggled"
}
/************************************************************************************************************
   Flashing Lights Handler
************************************************************************************************************/
private flashLights() {
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
		def initialActionOn = flashSwitches.collect{it.currentflashSwitch != "on"}
		def delay = 0L
		numFlashes.times {
			flashSwitches.eachWithIndex {s, i ->
				if (initialActionOn[i]) {
					s.on(delay: delay)
				}
				else {
					s.off(delay:delay)
				}
			}
			delay += onFor
			flashSwitches.eachWithIndex {s, i ->
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
