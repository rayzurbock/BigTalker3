definition(
    name: "BigTalker3-Parent",
    namespace: "rayzurbock",
    author: "rayzur@rayzurbock.com",
    description: "Let's talk about mode changes, switches, motions, and so on.",
    category: "Fun & Social",
	//singleInstance: true,
    iconUrl: "https://lowrance.cc/ST/icons/BigTalker.png",
    iconX2Url: "https://lowrance.cc/ST/icons/BigTalker@2x.png",
    iconX3Url: "https://lowrance.cc/ST/icons/BigTalker@2x.png")

preferences {
    page(name: "pageStart")
    page(name: "pageTalkNow")
    page(name: "pageConfigureSpeechDeviceType")
    page(name: "pageConfigureDefaults")
    page(name: "pageHelpPhraseTokens")
//End preferences
}

def pageStart(){
    state.childAppName = "BigTalker3-Child"
    state.parentAppName = "BigTalker3-Parent"
    state.namespace = "rayzurbock"
	setVersion()
    state.hubType = getHubType()
    LOGDEBUG("Hub Type: ${state.hubType}")
	setFormatting()
    state.supportedVoices = ["Ivy(en-us)","Joanna(en-us)","Joey(en-us)","Justin(en-us)","Matthew(en-us)","Kendra(en-us)","Kimberly(en-us)","Salli(en-us)","Amy(en-gb)","Brian(en-gb)","Emma(en-gb)","Miguel(es-us)","Penelope(es-us)"]
    if (checkConfig()) { 
        // Do nothing here, but run checkConfig() 
    } 
    dynamicPage(name: "pageStart", title: "Big Talker", install: false, uninstall: (app.getInstallationState() == "COMPLETE")){
    	def formatSettingRootStart = state.formatSettingRootStart
		def formatSettingRootEnd = state.formatSettingRootEnd
		def formatSettingOptionalStart = state.formatSettingOptionalStart
		def formatSettingOptionalEnd = state.formatSettingOptionalEnd
        def formatUlStart = state.formatUlStart
		def formatUlEnd = state.formatUlEnd
        def formatLiStart = state.formatLiStart
		def formatLiEnd = state.formatLiEnd
        def formatIStart = state.formatIStart
		def formatIEnd = state.formatIEnd
        def formatStrongStart = state.formatStrongStart
		def formatStrongEnd = state.formatStrongEnd
        def formatHr = state.formatHr
        def formatBr = state.formatBr
        def formatCenterStart = state.formatCenterStart
        def formatCenterEnd = state.formatCenterEnd
        section(){
        	LOGDEBUG("install state=${app.getInstallationState()}.")
            if (!(state.configOK)) { 
                href "pageConfigureSpeechDeviceType", title:"Configure", description:"Tap to configure"
            } else {
                //V1Method href "pageConfigureEvents", title: "Configure Events", description: "Tap to configure events"
                //href "pageStatus", title:"Status", description:"Tap to view status"
                href "pageConfigureDefaults", title: "Configure Defaults", description: "Tap to configure defaults"
				href "pageTalkNow", title:"Talk Now", description:"Tap to setup talk now" 
            }
        }
        section("Event Groups") {}
        section(){
        	def apps = getChildApps()?.sort{ it.label }
        	if (state.configOK) {
            	if (apps?.size() == 0) {
                	paragraph "You have not configured any event groups yet."
                    app(name: "BTEvt-", appName: state.childAppName, namespace: state.namespace, title: "Add Event Group", description: "Tap to configure event triggers", multiple: true, uninstall: false)
                } else {
            		app(name: "BTEvt-", appName: state.childAppName, namespace: state.namespace, title: "Add Event Group", description: "Tap to configure event triggers", multiple: true, uninstall: false)
                }
            }
        }
        section(){
        	if ((settings?.debugmode == true) && (settings?.resumePlay == true)) {
            		input name: "debug_pollnow", type: "bool", title: "DEBUG: Poll Now (simply toggle)", multiple: false, required: false, submitOnChange: true, defaultValue: false
            		if ((settings?.debug_pollnow == true) && (state?.lastDebugPollNow != true)) { poll() }
            		state.lastDebugPollNow = settings?.debug_pollnow
            }
        }
        section("About"){
            def AboutApp = ""
            AboutApp += "${formatHr}Big Talker is a SmartApp that can make your house talk depending on various triggered events.${formatBr}${formatBr}"
            AboutApp += "Pair with a Hubitat compatible audio device such as Sonos, Ubi, LANnouncer, Echo, Google Home Speaker and/or VLC Thing (running on your computer or Raspberry Pi)${formatBr}"
            paragraph(AboutApp)
			updateCheck()
			//checkButtons()
			displayVersionStatus()
        }
		section("${formatHr}${formatHr}${formatStrongStart}${formatCenterStart}Donations${formatCenterEnd}${formatStrongEnd}"){
				def DonateOptions = ""
				DonateOptions += "Big Talker is provided to the community for free.  It takes a lot of time to build and support any complex app.  If you wish to support the time and effort put into development you may submit a donation with one of the following:${formatBr}"
				DonateOptions += "${formatUlStart}"
                DonateOptions += "${formatLiStart}${formatStrongStart}Cash.me${formatStrongEnd} = <a target='_blank' href='https://cash.me/\$Lowrance'>https://cash.me/\$Lowrance</a> (use a debit card, it\'s free for both of us)${formatLiEnd}"
                DonateOptions += "${formatLiStart}${formatStrongStart}Venmo${formatStrongEnd} = <a target='_blank' href='https://venmo.com/code?user_id=2603208862072832399'>@BrianLowrance</a>${formatLiEnd}"
                DonateOptions += "${formatLiStart}${formatStrongStart}Paypal.me${formatStrongEnd} = <a target='_blank' href='https://paypal.me/brianlowrance'>https://paypal.me/brianlowrance</a> (They take a little since I setup my account as a business account)${formatLiEnd}"
				paragraph(DonateOptions)					
		}
    }
}

def pageTalkNow(){
    dynamicPage(name: "pageTalkNow", title: "Talk Now", install: false, uninstall: false){
        section(""){
        	def myTalkNowResume = false
            paragraph ("Speak the following phrase:\nNote: must differ from the last spoken phrase\n")
            if (true) {
            	input name: "talkNowVolume", type: "number", title: "Set volume to (overrides default):", required: false, submitOnChange: true
            	input name: "talkNowResume", type: "bool", title: "Enable audio resume", multiple: true, required: false, submitOnChange: true, defaultValue: (settings?.resumePlay == false) ? false : true
                input name: "talkNowVoice", type: "enum", title: "Select custom voice:", options: state.supportedVoices, required: false, submitOnChange: true
                myTalkNowResume = settings.talkNowResume
            }
            input name: "speechTalkNow", type: "text", title: "Speak phrase", required: false, submitOnChange: true
            input name: "talkNowSpeechDeviceMusicPlayer", type: "capability.musicPlayer", title: "Music players (e.g. Sonos, VLCThing)", multiple: true, required: false, submitOnChange: true
            input name: "talkNowSpeechDeviceSpeechSynthesis", type: "capability.speechSynthesis", title: "Speech speakers (e.g. Echo, Google Home, LANnouncer)", multiple: true, required: false, submitOnChange: true
            input name: "talkNowSpeechDeviceAudioNotification", type: "capability.audioNotification", title: "Audio notification devices", multiple: true, required: false, submitOnChange: true
            //LOGDEBUG("previoustext=${state.lastTalkNow} New=${settings.speechTalkNow}")
            def talkNowDevs = []
            if (settings.talkNowSpeechDeviceMusicPlayer) { talkNowDevs = talkNowDevs + settings.talkNowSpeechDeviceMusicPlayer }
            if (settings.talkNowSpeechDeviceSpeechSynthesis) { talkNowDevs = talkNowDevs + settings.talkNowSpeechDeviceSpeechSynthesis }
            if (settings.talkNowSpeechDeviceAudioNotification) { talkNowDevs = talkNowDevs + settings.talkNowSpeechDeviceAudioNotification }
            if (((!(state.lastTalkNow == settings.speechTalkNow)) && (talkNowDevs)) || (settings.speechTalkNow?.contains("%askalexa%"))){
                //Say stuff!
                if (true) {
                	myTalkNowResume = (myTalkNowResume == "") ? settings.resumeAudio : true //use global setting if TalkNow is not set
                	if (settings?.talkNowResume == null) {myTalkNowResume = true}  //default to true if not set.
				} else {
					//speechSynthesis does not use the following settings; make sure they are not null though
					myVolume = "100"
					myTalkNowResume = false
					personality = false
					myVoice = ""
				}
                def customevent = [displayName: 'BigTalker:TalkNow', name: 'TalkNow', value: 'TalkNow', descriptionText: "Talk Now"]
                def myVolume = getDesiredVolume(settings?.talkNowVolume)
                def myVoice = getMyVoice(settings.talkNowVoice)
                //def myVoice = (!(talkNowVoice == null || talkNowVoice == "")) ? talkNowVoice : (settings?.speechVoice ? settings.speechVoice : "Sallie(en-us)")
                def personality = false
                LOGDEBUG ("TalkNow Voice=${myVoice}")
                Talk("Talk Now", settings.speechTalkNow, talkNowDevs, myVolume, myTalkNowResume, personality, myVoice, customevent)
                state.lastTalkNow = settings.speechTalkNow
            }
        }
        section("Help"){
            href "pageHelpPhraseTokens", title:"Phrase Tokens", description:"Tap for a list of phrase tokens"
        }
    }
}

def getMyVoice(deviceVoice){
    def myVoice = "Not Used"
    if (true) {
    	LOGDEBUG("getMyVoice[parent]: deviceVoice=${deviceVoice ? deviceVoice : "Not selected"}")
        LOGDEBUG("getMyVoice[parent]: settings.speechVoice=${settings?.speechVoice}")
		myVoice = (!(deviceVoice == null || deviceVoice == "")) ? deviceVoice : (settings?.speechVoice ? settings?.speechVoice : "Salli(en-us)")
    }
    return myVoice
}

def pageHelpPhraseTokens(){
	//KEEP IN SYNC WITH CHILD!
        dynamicPage(name: "pageHelpPhraseTokens", title: "Available Phrase Tokens", install: false, uninstall:false){
    	def formatSettingRootStart = state.formatSettingRootStart
		def formatSettingRootEnd = state.formatSettingRootEnd
		def formatSettingOptionalStart = state.formatSettingOptionalStart
		def formatSettingOptionalEnd = state.formatSettingOptionalEnd
        def formatUlStart = state.formatUlStart
		def formatUlEnd = state.formatUlEnd
        def formatLiStart = state.formatLiStart
		def formatLiEnd = state.formatLiEnd
        def formatIStart = state.formatIStart
		def formatIEnd = state.formatIEnd
        def formatStrongStart = state.formatStrongStart
		def formatStrongEnd = state.formatStrongEnd
       section("The following tokens can be used in your event phrases and will be replaced as listed:"){
       	   def AvailTokens = ""
           AvailTokens += "${formatUlStart}"
           AvailTokens += "${formatLiStart}${formatStrongStart}%askalexa%${formatStrongEnd} = Send phrase to AskAlexa message queue${formatLiEnd}"
		   AvailTokens += "${formatLiStart}${formatStrongStart}%groupname%${formatStrongEnd} = Name that you gave for the event group${formatLiEnd}"
           AvailTokens += "${formatLiStart}${formatStrongStart}%date%${formatStrongEnd} = Current date; January 01 20xx${formatLiEnd}"
           AvailTokens += "${formatLiStart}${formatStrongStart}%day%${formatStrongEnd} = Current day; Monday${formatLiEnd}"
           AvailTokens += "${formatLiStart}${formatStrongStart}%devicename%${formatStrongEnd} = Triggering devices display name${formatLiEnd}"
           AvailTokens += "${formatLiStart}${formatStrongStart}%devicetype%${formatStrongEnd} = Triggering device type; motion, switch, etc${formatLiEnd}"
           AvailTokens += "${formatLiStart}${formatStrongStart}%devicechange%${formatStrongEnd} = State change that occurred; on/off, active/inactive, etc...${formatLiEnd}"
           AvailTokens += "${formatLiStart}${formatStrongStart}%description%${formatStrongEnd} = The description of the event that is to be displayed to the user in the mobile application.${formatLiEnd}"
           AvailTokens += "${formatLiStart}${formatStrongStart}%locationname%${formatStrongEnd} = Hub location name; home, work, etc${formatLiEnd}"
           AvailTokens += "${formatLiStart}${formatStrongStart}%lastmode%${formatStrongEnd} = Last hub mode; home, away, etc${formatLiEnd}"
           AvailTokens += "${formatLiStart}${formatStrongStart}%mode%${formatStrongEnd} = Current hub mode; home, away, etc${formatLiEnd}"
           AvailTokens += "${formatLiStart}${formatStrongStart}%mp3(url)%${formatStrongEnd} = Play hosted MP3 file; URL should be http://www.domain.com/path/file.mp3"
           AvailTokens += "${formatUlStart}${formatLiStart}${formatIStart}No other tokens or phrases can be used with %mp3(url)%${formatIEnd}${formatLiEnd}${formatUlEnd}"
		   AvailTokens += "${formatLiEnd}"
           AvailTokens += "${formatLiStart}${formatStrongStart}%time%${formatStrongEnd} = Current hub time; HH:mm am/pm${formatStrongEnd}${formatLiEnd}"
           AvailTokens += "${formatLiStart}${formatStrongStart}%value%${formatStrongEnd} = Wattage value; only applicable in Power Meter events${formatLiEnd}"
		   AvailTokens += "${formatUlEnd}"
           AvailTokens += "${formatLiStart}${formatStrongStart}%weathercurrent%${formatStrongEnd} = Current weather based on hub location${formatLiEnd}"
           AvailTokens += "${formatLiStart}${formatStrongStart}%weathertoday%${formatStrongEnd} = Today's weather forecast* based on hub location${formatLiEnd}"
           AvailTokens += "${formatLiStart}${formatStrongStart}%weathertonight%${formatStrongEnd} = Tonight's weather forecast* based on hub location${formatLiEnd}"
           AvailTokens += "${formatLiStart}${formatStrongStart}%weathertomorrow%${formatStrongEnd} = Tomorrow's weather forecast* based on hub location${formatLiEnd}"
           paragraph(AvailTokens)
       }
   }
}

def pageConfigureSpeechDeviceType(){
	def myNextPage = ""
    if (!(state?.installed == true)) { state.installed = false; state.speechDeviceType = "auto"; myNextPage = "pageConfigureDefaults"}
    dynamicPage(name: "pageConfigureSpeechDeviceType", title: "Configure", nextPage: myNextPage, install: false, uninstall: false) {
        //section ("Speech Device Type Support"){
        section (){
			if (state.installed == true) { 
				paragraph "<font color=red><strong>PROCEED WITH CAUTION!</strong></font>\n<font color=red><strong>WARNING!</strong></font> If you change this setting after the app has been configured, you will need to update your selected default and custom speech devices!\n <strong>To Cancel:</strong> Don't move the switch and press Next (or Done)\n<font color=red><strong>PROCEED WITH CAUTION!</strong></font>"
			}
            paragraph "${app.label} supports music players, speech speakers, and audio notification devices together -- no mode switching needed."
            paragraph "'Music Player' typically supports devices such as Sonos, VLCThing.\n'Speech Synthesis' typically supports devices such as Google Home Speaker, Echo, Ubi and LANnouncer.\n'Audio Notification' supports devices that can play a notification track directly.\n\nBig Talker automatically uses the correct audio capability for each device you select -- no mode switching needed."
            state.speechDeviceType = "auto"
            //paragraph "Click Next to continue configuration...\n"
        }
    }
//End pageConfigureSpeechDeviceType()
}

def pageConfigureDefaults(){
	unsubscribe()
	myRunIn(3, initSubscribe)
	state.mode = location.mode
	state.lastMode = state.mode
    if (state?.installed == true) { 
       state.dynPageProperties = [
            name:      "pageConfigureDefaults",
            title:     "Configure Defaults",
            install:   false,
            uninstall: false,
            //nextPage:  "pageConfigureEvents"
        ]
    } else {
       state.dynPageProperties = [
            name:      "pageConfigureDefaults",
            title:     "Configure Defaults",
            install:   true,
            uninstall: false
        ]
    }
    return dynamicPage(state.dynPageProperties) {
    //dynamicPage(name: "pageConfigureDefaults", title: "Configure Defaults", nextPage: "${myNextPage}", install: false, uninstall: false) {
		section("${state.formatSettingRootStart}Talk with:${state.formatSettingRootEnd}"){
           input "speechDeviceDefaultMusicPlayer", "capability.musicPlayer", title: "Music players (default, e.g. Sonos, VLCThing)", multiple: true, required: false, submitOnChange: false
           input "speechDeviceDefaultSpeechSynthesis", "capability.speechSynthesis", title: "Speech speakers (default, e.g. Echo, Google Home, LANnouncer)", multiple: true, required: false, submitOnChange: false
           input "speechDeviceDefaultAudioNotification", "capability.audioNotification", title: "Audio notification devices (default)", multiple: true, required: false, submitOnChange: false
        }
        if (true) {
            section ("${state.formatSettingRootStart}Adjust volume during announcement <I>(optional; Supports: Sonos, VLC-Thing):${state.formatSettingRootEnd}"){
            	input "speechMinimumVolume", "number", title: "Minimum volume for announcement (0-100%, Default: 50%):", required: false
                input "speechVolume", "number", title: "Set volume during announcement (0-100%):", required: false
                input "speechVoice", "enum", title: "Select voice:", options: state.supportedVoices, required: true, defaultValue: "Salli(en-us)"
            }
            section ("${state.formatSettingRootStart}Attempt to resume playing audio <I>(optional; Supports: Sonos, VLC-Thing):${state.formatSettingRootEnd}"){
            	input "resumePlay", "bool", title: "Resume Play:", required: true, defaultValue: true
                input "allowScheduledPoll", "bool", title: "Enable polling device status (recommended)", required: true, defaultValue: true
            }
        }
        section (""){
            input "speechModesDefault", "mode", title: "${state.formatSettingRootStart}Talk only while in these modes (default)${state.formatSettingRootEnd}", multiple: true, required: true, submitOnChange: false
        }
        section ("${state.formatSettingRootStart}Only between these times:${state.formatSettingRootEnd}"){
            input "defaultStartTime", "time", title: "Don't talk before: ", required: false, submitOnChange: true
            input "defaultEndTime", "time", title: "Don't talk after: ", required: (!(settings.defaultStartTime == null)), submitOnChange: true
        }
        section(){
            input "personalityMode", "bool", title: "${state.formatSettingRootStart}Allow Personality?${state.formatSettingRootEnd}", required: true, defaultValue: false
            input "customPersonalityPre", "text", title: "${state.formatSettingRootStart}Custom personality phrases spoken BEFORE the announcement (comma separated, optional)${state.formatSettingRootEnd}", required: false
            input "customPersonalityPost", "text", title: "${state.formatSettingRootStart}Custom personality phrases spoken AFTER the announcement (comma separated, optional)${state.formatSettingRootEnd}", required: false
            input "debugmode", "bool", title: "${state.formatSettingRootStart}Enable debug logging${state.formatSettingRootEnd}", required: true, defaultValue: false, submitOnChange: true
            input "allowAskAlexa", "bool", title: "${state.formatSettingRootStart}Allow the %askalexa% token to enqueue messages for AskAlexa${state.formatSettingRootEnd}", required: false, defaultValue: false
			if (settings.debugmode != state?.lastDebugmodeSetting) {
				state.lastDebugmodeSetting = settings.debugmode
				state.debugAutoOff = false
				unschedule("disableDebug")
				if (debugmode) {
					state.debugMode = true; myRunIn(1800, disableDebug); LOGTRACE("Debug logging has been enabled.  Will auto-disable in 30 minutes.")
				} else {
					state.debugMode = false; LOGTRACE("Debug logging is not enabled.")
				}
			} else if (state?.debugAutoOff == true && debugmode) {
				//Auto-off already took effect; stay off until the user toggles the setting again
			} else if (state?.debugMode == null) {
				state.debugMode = (debugmode == true)
			}
        }
    }
}

def installed() {
	state.installed = true
    //LOGTRACE("Installed with settings: ${settings}")
    LOGTRACE("Installed (Parent Version: ${state.version})")
	initialize()
    if (settings?.allowScheduledPoll != false){ 
    	myRunIn(60, poll) 
    }
//End installed()
}

def updated() {
    unschedule()
    state.installed = true
	//LOGTRACE("Updated with settings: ${settings}")
    LOGTRACE("Updated settings (Parent Version: ${state.version})")
    unsubscribe()
    initialize()
    if (settings?.allowScheduledPoll != false){ 
    	myRunIn(60, poll) 
    }
//End updated()
}

def checkConfig() {
    def configErrorList = ""
    if (!(state.speechDeviceType)){
       state.speechDeviceType = "auto" //Set a default if the app was updated and didn't contain settings.speechDeviceType
    }
    if (settings?.allowScheduledPoll == true && settings?.resumePlay == true) { state.allowScheduledPoll = true }
    if (settings?.allowScheduledPoll == null || settings?.resumePlay == null) { state.allowScheduledPoll = true }
	if (settings?.allowScheduledPoll == false || settings?.resumePlay == false) { state.allowScheduledPoll = false}
//    if (!(getDefaultSpeechDevices())){
//        configErrorList += "  ** Default speech device(s) not selected,"
//    }
    if (!(state.installed == true)) {
	    configErrorList += "  ** state.installed not True,"
	}
    if (!(configErrorList == "")) { 
        LOGDEBUG ("checkConfig() returning FALSE (${configErrorList})")
        state.configOK = false
        return false //Errors occurred.  Config check failed.
    } else {
        LOGDEBUG ("checkConfig() returning TRUE (${configErrorList})")
        state.configOK = true
        return true
    }
}

def initialize() {
    if (!(checkConfig())) { 
        def msg = ""
        msg = "ERROR: App not properly configured!  Can't start.\n"
        msg += "ERRORs:\n${state.configErrorList}"
        LOGTRACE(msg)
        state.polledDevices = ""
        return //App not properly configured, exit, don't subscribe
    }
	version()
	LOGTRACE("Initialized (Parent Version: ${state.version})")
    state.lastMode = location.mode
    state.lastTalkNow = settings.speechTalkNow
	initSubscribe()
//End initialize()
}

def initSubscribe() {
	//Subscribe Mode
	LOGDEBUG("Subscribed")
    subscribe(location, "mode", onModeChangeEvent) //Keep track of mode changes
}

def processPhraseVariables(appname, phrase, evt, mode = null){
    if (mode == null || mode == "") { mode = "auto" }
    try {
    	def zipCode = location.zipCode
    	def mp3Url = ""
    	if (phrase.toLowerCase().contains("%mp3(")) {
    		if (phrase.toLowerCase().contains(".mp3)%")) {
            	def phraseMP3Start = (phrase.toLowerCase().indexOf("%mp3(") + 5)
            	def phraseMP3End = (phrase.toLowerCase().indexOf(".mp3)%"))
            	mp3Url = phrase.substring(phraseMP3Start, phraseMP3End)
            	LOGDEBUG("MP3 URL: ${mp3Url}")
            	phrase = phrase.replace("%mp3(","")
            	phrase = phrase.replace(".mp3)%", ".mp3")
            	phrase = phrase.replace (" ", "%20")
            	phrase = phrase.replace ("+", "%2B")
            	phrase = phrase.replace ("-", "%2D")
        	} else {
	            phrase = "Invalid M P 3 URL found in M P 3 token"
    	    }
            if (!isSafeUrl(mp3Url)) {
                phrase = "Rejected unsafe MP3 URL"
            }
	        return phrase
    	}
    	if (phrase.toLowerCase().contains(" percent ")) { phrase = phrase.replace(" percent ","%") }
    	if (phrase.toLowerCase().contains("%groupname%")) {
    		phrase = replaceTokenIgnoreCase(phrase, '%groupname%', appname)
    	}
    	if (phrase.toLowerCase().contains("%devicename%")) {
	    	try {
    	    	phrase = replaceTokenIgnoreCase(phrase, '%devicename%', evt.displayName)  //User given name of the device triggering the event
        	}
        	catch (ex) { 
        		LOGDEBUG("evt.displayName failed; trying evt.device.displayName")
        		try {
                	phrase = replaceTokenIgnoreCase(phrase, '%devicename%', evt.device.displayName) //User given name of the device triggering the event
            	}
            	catch (ex2) {
	            	LOGDEBUG("evt.device.displayName filed; trying evt.device.name")
    	            try {
        	        	phrase = replaceTokenIgnoreCase(phrase, '%devicename%', evt.device.name) //Driver name for the device triggering the event
            	    }
                	catch (ex3) {
                		LOGDEBUG("evt.device.name filed; Giving up")
                    	phrase = replaceTokenIgnoreCase(phrase, '%devicename%', "Device Name Unknown")
                	}
            	}
       		}
    	}
    	if (phrase.toLowerCase().contains("%devicetype%")) {phrase = replaceTokenIgnoreCase(phrase, '%devicetype%', evt.name)}  //Device type: motion, switch, etc...
    	if (phrase.toLowerCase().contains("%devicechange%")) {phrase = replaceTokenIgnoreCase(phrase, '%devicechange%', evt.value)}  //State change that occurred: on/off, active/inactive, etc...
    	if (phrase.toLowerCase().contains("%description%")) {phrase = replaceTokenIgnoreCase(phrase, '%description%', evt.descriptionText)}  //Description of the event which occurred via device-specific text`
    	if (phrase.toLowerCase().contains("%locationname%")) {phrase = replaceTokenIgnoreCase(phrase, '%locationname%', location.name)}
    	if (phrase.toLowerCase().contains("%lastmode%")) {phrase = replaceTokenIgnoreCase(phrase, '%lastmode%', state.lastMode)}
    	if (phrase.toLowerCase().contains("%mode%")) {phrase = replaceTokenIgnoreCase(phrase, '%mode%', location.mode)}
    	if (phrase.toLowerCase().contains("%time%")) {
    		phrase = replaceTokenIgnoreCase(phrase, '%time%', getTimeFromCalendar(false,true))
        	if ((phrase.toLowerCase().contains("00:")) && (phrase.toLowerCase().contains("am"))) {phrase = replaceTokenIgnoreCase(phrase, '00:', "12:")}
        	if ((phrase.toLowerCase().contains("24:")) && (phrase.toLowerCase().contains("am"))) {phrase = replaceTokenIgnoreCase(phrase, '24:', "12:")}
        	if ((phrase.toLowerCase().contains("0:")) && (!phrase.toLowerCase().contains("10:")) && (phrase.toLowerCase().contains("am"))) {phrase = replaceTokenIgnoreCase(phrase, '0:', "12:")}
    	}
    	if (phrase.toLowerCase().contains("%weathercurrent%")) {phrase = replaceTokenIgnoreCase(phrase, '%weathercurrent%', getWeather("current", zipCode)); phrase = adjustWeatherPhrase(phrase)}
    	if (phrase.toLowerCase().contains("%weathertoday%")) {phrase = replaceTokenIgnoreCase(phrase, '%weathertoday%', getWeather("today", zipCode)); phrase = adjustWeatherPhrase(phrase)}
    	if (phrase.toLowerCase().contains("%weathertonight%")) {phrase = replaceTokenIgnoreCase(phrase, '%weathertonight%', getWeather("tonight", zipCode));phrase = adjustWeatherPhrase(phrase)}
    	if (phrase.toLowerCase().contains("%weathertomorrow%")) {phrase = replaceTokenIgnoreCase(phrase, '%weathertomorrow%', getWeather("tomorrow", zipCode));phrase = adjustWeatherPhrase(phrase)}
	    if (mode == "capability.speechSynthesis" || mode == "capability.audioNotification"){
	        //Some TTS engines pronounce commas as "Dash", so convert them to dashes
	        if (phrase.contains(",")) { phrase = phrase.replace(","," - ") }
	        //if (phrase.contains(".")) { phrase = phrase.replace("."," - ") }
	    }
	    if (phrase.contains('"')) { phrase = phrase.replace('"',"") }
	    if (phrase.contains("'")) { phrase = phrase.replace("'","") }
	    if (phrase.toLowerCase().contains("10s")) { phrase = replaceTokenIgnoreCase(phrase, "10s","tens") }
	    if (phrase.toLowerCase().contains("20s")) { phrase = replaceTokenIgnoreCase(phrase, "20s","twenties") }
	    if (phrase.toLowerCase().contains("30s")) { phrase = replaceTokenIgnoreCase(phrase, "30s","thirties") }
	    if (phrase.toLowerCase().contains("40s")) { phrase = replaceTokenIgnoreCase(phrase, "40s","fourties") }
	    if (phrase.toLowerCase().contains("50s")) { phrase = replaceTokenIgnoreCase(phrase, "50s","fifties") }
	    if (phrase.toLowerCase().contains("60s")) { phrase = replaceTokenIgnoreCase(phrase, "60s","sixties") }
	    if (phrase.toLowerCase().contains("70s")) { phrase = replaceTokenIgnoreCase(phrase, "70s","seventies") }
	    if (phrase.toLowerCase().contains("80s")) { phrase = replaceTokenIgnoreCase(phrase, "80s","eighties") }
	    if (phrase.toLowerCase().contains("90s")) { phrase = replaceTokenIgnoreCase(phrase, "90s","nineties") }
	    if (phrase.toLowerCase().contains("100s")) { phrase = replaceTokenIgnoreCase(phrase, "100s","one hundreds") }
	    if (phrase.toLowerCase().contains("%askalexa%")) {
	    	phrase=replaceTokenIgnoreCase(phrase, "%askalexa%","")
	        if (!(phrase == "") && (!(phrase == null))){
	    		LOGTRACE("Sending to AskAlexa: ${phrase}.")
		    	sendLocationEvent(name: "AskAlexaMsgQueue", value: "BigTalker", isStateChange: true, descriptionText: phrase.replaceAll(/<[^>]*>?/, ''))
	        }else{
	        	LOGERROR("Phrase only contained %askalexa%. Nothing to say/send.")
	        }
	    }
	    if (phrase.toLowerCase().contains("%date%")) {
	    	phrase=replaceTokenIgnoreCase(phrase, "%date%",(new Date().format( 'MMMM dd' )))
	    }
	    if (phrase.toLowerCase().contains("%day%")) {
	    	phrase=replaceTokenIgnoreCase(phrase, "%day%",(new Date().format('EEEE',location.timeZone)))
	    }
	    if (phrase.contains("%")) { phrase = phrase.replace("%"," percent ") }
	    return phrase
	} catch(ex) { 
		LOGTRACE("There was a problem processing your desired phrase: ${phrase}. ${ex}")
    	phrase = "Sorry, there was a problem processing your desired BigTalker phrase token."
    	return phrase
	}
}

def addPersonalityToPhrase(phrase, evt){
	LOGDEBUG("addPersonalityToPhrase(${phrase},${evt})")
    def response = new String[20]
    response[0] = ""
    def options = 0
    def genericresponse = new String[20]
    genericresponse[0] = ""
    def genericoptions = 0
    def myRandom = 0
    //SWITCHES BEGIN
    if (evt.value == "on") {
    	if (phrase.contains("light")){
        	options = 16
  			response[1] = "{POST}please don't forget to turn the light off"
           	response[2] = "{POST}night vision goggles would do the same but I guess they are more expensive."
            response[3] = "{POST}Thanks Thomas Edison!"
            response[4] = "{POST}Wow, this is bright!"
            response[5] = "{POST}Where are my sunglasses."
            response[6] = "{POST}there goes the electricity bill!"
            response[7] = "{POST}the same old thing everyday."
            response[8] = "{POST}It is about time it was awfully dark!"
            response[9] = "{POST}Glad you are here, I was lonely"
            response[10] = "{POST}Is it time for us to play?"
            response[13] = "{PRE}Well, hello there"
            response[14] = "{PRE}Look who's back"
            response[15] = "{POST}I could get used to this"
            response[16] = "{POST}let there be light!"
            response[11] = "{PRE}Oh, Hi"
            response[12] = "{PRE}Oh, Hi there"
        } else {
        	//Something turned on, but it wasn't a light
        	options = 8
            response[1] = "{POST}there goes the electricity bill!"
            response[2] = "{POST}the same old thing everyday."
            response[3] = "{PRE}Oh, Hi"
            response[4] = "{PRE}Oh, Hi there"
            response[5] = "{PRE}Well, hello there"
            response[6] = "{PRE}Look who's back"
            response[7] = "{POST}I could get used to this"
            response[8] = "{POST}it's about time!"
        }
    }
    if (evt.value == "off") {
    	if (phrase.contains("light")){
        	options = 16
           	response[1] = "{POST}It's about time!"
            response[2] = "{POST}time to save some money!"
            response[3] = "{POST}wow, it's dark"
            response[4] = "{POST}going green are we?"
            response[5] = "{POST}I'll still be here, in the dark."
            response[6] = "{POST}Hey! You know I am afraid of the dark."
            response[7] = "{POST}Please don't leave me alone in the dark."
            response[8] = "{POST}Good thing you turned that off it was hurting my eyes!"
            response[9] = "{POST}You really like saving money!"
            response[10] = "{POST}Is it time to go to sleep?"
            response[11] = "{PRE}Oh, Hi"
            response[12] = "{PRE}Oh, Hi there"
            response[13] = "{PRE}Going somewhere?"
            response[14] = "{PRE}Nighty night"
            response[15] = "{POST}I hope you come back soon"
            response[16] = "{POST}Finally, some peace and quiet"
        } else {
        	//Something turned off, but it wasn't a light
        	options = 9
        	response[1] = "{POST}It's about time!"
            response[2] = "{POST}time to save some money!"
            response[3] = "{POST}going green are we?"
            response[4] = "{PRE}Oh, Hi"
            response[5] = "{PRE}Oh, Hi there"
            response[6] = "{PRE}Going somewhere?"
            response[7] = "{PRE}Well, hello there"
            response[8] = "{POST}I could get used to this"
            response[9] = "{POST}Finally, some peace and quiet"
        }
    }
    //SWITCHES END
    def UseGenericRandom = 0
    myRandom = (new Random().nextInt(10))
    if (myRandom == 1 || myRandom == 4 || myRandom == 7) {
    	//GENERIC RESPONSES BEGIN
    	genericoptions = 8
    	genericresponse[1] = "{PRE}Hey there"
    	genericresponse[2] = "{PRE}Don't mean to bother but"
        genericresponse[3] = "{PRE}All I know is"
        genericresponse[4] = "{POST}that is all I know."
        genericresponse[5] = "{PRE}Just so you know,"
        genericresponse[6] = "{PRE}Heads up!"
        genericresponse[7] = "{PRE}By the way,"
        genericresponse[8] = "{POST}you're welcome."
        //Append user-defined custom personality phrases
        def customPre = 8
        if (settings?.customPersonalityPre) {
        	settings.customPersonalityPre.split(',').each {
        		customPre++
        		if (customPre <= 19) { genericresponse[customPre] = "{PRE}${it.trim()}" }
        	}
        }
        def customPostStart = customPre
        if (settings?.customPersonalityPost) {
        	settings.customPersonalityPost.split(',').each {
        		customPostStart++
        		if (customPostStart <= 19) { genericresponse[customPostStart] = "{POST}${it.trim()}" }
        	}
        }
        genericoptions = Math.max(8, Math.max(customPre, customPostStart))
    	//GENERIC RESPONSES END
    	myRandom = (new Random().nextInt(genericoptions))
        LOGDEBUG("genericoptions=${genericoptions};myRandom=${myRandom};phrase=${genericresponse[myRandom]}")
    	if (genericresponse[myRandom].contains("{PRE}")) {
    		genericresponse[myRandom] = genericresponse[myRandom].replace("{PRE}", "")
        	phrase = genericresponse[myRandom] + ", " + phrase
    	}
    	if (genericresponse[myRandom].contains("{POST}")) {
    		genericresponse[myRandom] = genericresponse[myRandom].replace("{POST}", "")
        	phrase = phrase + ", " + genericresponse[myRandom]
    	}
        return phrase
    }
    if (options == 0) { return phrase }
    myRandom = (new Random().nextInt(options))
    LOGDEBUG("options=${options};myRandom=${myRandom};phrase=${response[myRandom]}")
    if (response[myRandom].contains("{PRE}")) {
    	response[myRandom] = response[myRandom].replace("{PRE}", "")
        phrase = response[myRandom] + ", " + phrase
    }
    if (response[myRandom].contains("{POST}")) {
    	response[myRandom] = response[myRandom].replace("{POST}", "")
        phrase = phrase + ", " + response[myRandom]
    }
    return phrase
}

def adjustWeatherPhrase(phraseIn){
    def phraseOut = ""
    phraseOut = phraseIn.toUpperCase()
    phraseOut = phraseOut.replace(" N ", " North ")
    phraseOut = phraseOut.replace(" S ", " South ")
    phraseOut = phraseOut.replace(" E ", " East ")
    phraseOut = phraseOut.replace(" W ", " West ")
    phraseOut = phraseOut.replace(" NNE ", " North Northeast ")
    phraseOut = phraseOut.replace(" NNW ", " North Northwest ")
    phraseOut = phraseOut.replace(" SSE ", " South Southeast ")
    phraseOut = phraseOut.replace(" SSW ", " South Southwest ")
    phraseOut = phraseOut.replace(" ENE ", " East Northeast ")
    phraseOut = phraseOut.replace(" ESE ", " East Southeast ")
    phraseOut = phraseOut.replace(" WNW ", " West Northeast ")
    phraseOut = phraseOut.replace(" WSW ", " West Southwest ")
    phraseOut = phraseOut.replace(" MPH", " Miles Per Hour")
    phraseOut = phraseOut.replace(" MM)", " Milimeters ")
    LOGDEBUG ("Adjust Weather: In=${phraseIn} Out=${phraseOut}")
    return phraseOut
}

def Talk(appname, phrase, customSpeechDevice, volume, resume, personality, voice, evt, mode = null){
	if (mode == null || mode == "") { mode = "auto" }
	if (mode == "auto") {
		//Auto: split picked devices by capability and send each the right way
		def candidateDevices = customSpeechDevice ?: getDefaultSpeechDevices()
		def sSDevs = []
		def aNDevs = []
		def mPDevs = []
		candidateDevices.each { d ->
			if (d.hasCommand("speak")) {
				sSDevs = sSDevs + d
			} else if ((d.hasCommand("playTrack") || d.hasCommand("playTrackAndRestore")) && (d.hasAttribute("status") || d.hasAttribute("level") || d.hasAttribute("mute"))) {
				mPDevs = mPDevs + d
			} else if (d.hasCommand("playTrack") || d.hasCommand("playTrackAndRestore")) {
				aNDevs = aNDevs + d
			} else {
				LOGERROR("Talk: ${d.displayName} does not support a usable audio capability - skipped.")
			}
		}
		if (sSDevs) { Talk(appname, phrase, sSDevs, volume, resume, personality, voice, evt, "capability.speechSynthesis") }
		if (aNDevs) { Talk(appname, phrase, aNDevs, volume, resume, personality, voice, evt, "capability.audioNotification") }
		if (mPDevs) { Talk(appname, phrase, mPDevs, volume, resume, personality, voice, evt, "capability.musicPlayer") }
		return
	}
	def myDelay = 100
    def myVoice = settings?.speechVoice
    if (myVoice == "" || myVoice == null) { myVoice = "Salli(en-us)" } 
    if (!(voice == "" || voice == null)) { 
        myVoice = voice
    }
   	myVoice = myVoice.replace("(en-us)","")
   	myVoice = myVoice.replace("(en-gb)","")
   	myVoice = myVoice.replace("(es-us)","")
    if (mode == "capability.musicPlayer") { 
    	myDelay = TalkQueue(appname, phrase, customSpeechDevice, volume, resume, personality, voice, evt) 
        atomicState.lastTalkTime = now()
    }
	def currentSpeechDevices = []
   	def smartAppSpeechDevice = false
    def playAudioFile = false
   	def spoke = false
    LOGDEBUG ("TALK(app=${appname},customdevice=${customSpeechDevice},volume=${volume},resume=${resume},personality=${personality},myDelay=${myDelay},voice=${myVoice},evt=${evt},phraseLen=${phrase?.length() ?: 0})")
   	if ((phrase?.toLowerCase())?.contains("%askalexa%")) {smartAppSpeechDevice = true}
   	if (!(phrase == null) && !(phrase == "")) {
		phrase = processPhraseVariables(appname, phrase, evt, mode)
	    if (personality && !(phrase.toLowerCase().contains(".mp3"))) { phrase = addPersonalityToPhrase(phrase, evt) }
	}
	if (phrase == null || phrase == "") {
   		LOGERROR(processPhraseVariables(appname, "BigTalker - Check configuration. Phrase is empty for %devicename%", evt))
  	}
	if (resume == null) { resume = true }
	if ((mode == "capability.musicPlayer") && (!( phrase==null ) && !(phrase==""))){
		state.sound = ""
		state.ableToTalk = false
		if (true) {
			LOGTRACE("TALK(${appname}.${evt.name})|mP@|${volume} >> ${phrase}")
            if (resume) { LOGTRACE("TALK(${appname}.${evt.name})|mP| Resume is desired") } else { LOGTRACE("TALK(${appname}.${evt.name})|mP| Resume is not desired") }
			if (!(phrase.toLowerCase().contains(".mp3"))){
            	try {
					state.sound = textToSpeech(phrase instanceof List ? phrase[0] : phrase, myVoice) 
					state.ableToTalk = true
				} catch(e) {
					LOGERROR("TALK(${appname}.${evt.name})|mP| Platform issue (textToSpeech)? ${e}")
					//Try Again
					try {
						LOGTRACE("TALK(${appname}.${evt.name})|mP| Trying textToSpeech function again...")
					state.sound = textToSpeech(phrase instanceof List ? phrase[0] : phrase, (settings?.speechVoice ?: "Salli(en-us)").replaceAll(/\(.*\)/, ''))
					state.ableToTalk = true
					} catch(ex) {
						LOGERROR("TALK(${appname}.${evt.name})|mP| Platform issue (textToSpeech)? I tried textToSpeech() twice, ${state.hubType} wouldn't convert/process.  I give up, Sorry..")
					} //try again before final error(ableToTalk)
				} //try (ableToTalk)
            } else {
            	if (isSafeUrl(phrase)) {
            	LOGTRACE("TALK(${appname}.${evt.name})|mP| MP3=${phrase}")
            	def sound = [uri:phrase, duration:10]
                state.sound = sound
                playAudioFile = true
                state.ableToTalk = true
                LOGTRACE("Sound=${state.sound}")
            } else {
                LOGERROR("TALK(${appname}.${evt.name})|mP| Rejected unsafe MP3 URL: ${phrase}")
            }
            }
   	        if ((state?.allowScheduledPoll != false) && (resume)) {
				unschedule("poll")
				LOGDEBUG("TALK(${appname}.${evt.name})|mP| Delaying polling for 120 seconds")
				myRunIn(120, poll)
           	}
			if (state.ableToTalk){
				state.sound.duration = (state.sound.duration.toInteger() + 5).toString()  //Try to prevent cutting out, add seconds to the duration
				if (!(customSpeechDevice == null)) {
					currentSpeechDevices = customSpeechDevice
				} else {
					//Use Default Speech Device
					currentSpeechDevices = getDefaultSpeechDevices()
				} //if (!(customSpeechDevice == null))
				LOGTRACE("TALK(${appname}.${evt.name})|mP| Last poll: ${state.lastPoll}")
				//Iterate Speech Devices and talk
				def attrs = currentSpeechDevices.supportedAttributes
				currentSpeechDevices.each(){
					LOGDEBUG("TALK(${appname}.${evt.name})|mP| attrs=${attrs}")
					def currentStatus = ""
                   	try {
                    	currentStatus = it?.latestValue("status")
   	                } catch (ex) { LOGDEBUG("ERROR getting device currentStatus") }
					def currentTrack = ""
           	        try {
              	    	currentTrack = it?.latestState("trackData")?.jsonValue
                   	} catch (ex) { LOGDEBUG("ERROR getting device currentTrack") }
					def currentVolume = 0
					if (it.hasAttribute("level")) { try { currentVolume = it.latestValue("level")?.toString()?.toInteger() ?: 0 } catch (ex) { currentVolume = 0 } }
					if (it.hasAttribute("volume")) { try { currentVolume = it.latestValue("volume")?.toString()?.toInteger() ?: 0 } catch (ex) { currentVolume = 0 } }
					if (currentVolume == 0) { LOGDEBUG("ERROR getting device currentVolume") }
                   	def minimumVolume = 50
                   	if (settings?.speechMinimumVolume >= 0) {minimumVolume = settings.speechMinimumVolume}
                   	if (minimumVolume > 100) {minimumVolume = 100}
                   	def desiredVolume = volume
                   	//try {
                   	//	desiredVolume = settings?.speechVolume
                   	//} catch (ex) { LOGDEBUG("ERROR getting desired default volume"); desiredVolume = -1 }
                   	if (desiredVolume > 100) {desiredVolume = 100}
                   	LOGDEBUG("TALK(${appname}.${evt.name})|mP| currentStatus:${currentStatus}")
					LOGDEBUG("TALK(${appname}.${evt.name})|mP| currentTrack:${currentTrack}")
					LOGDEBUG("TALK(${appname}.${evt.name})|mP| currentVolume:${currentVolume}")
					LOGDEBUG("TALK(${appname}.${evt.name})|mP| Sound: ${state.sound.uri} , ${state.sound.duration}")
					if (desiredVolume > -1){ 
                   		LOGTRACE("TALK(${appname}.${evt.name})|mP| ${it.displayName} | Volume: ${currentVolume}, Desired Volume: ${desiredVolume}")
                   	} else {
	                    if (!(currentVolume >= minimumVolume)) {
   		                	LOGTRACE("TALK(${appname}.${evt.name})|mP| ${it.displayName} | Volume: ${currentVolume}, Minimum Volume: ${minimumVolume}; adjusting.")
       		            } else {
           		        	LOGTRACE("TALK(${appname}.${evt.name})|mP| ${it.displayName} | Volume: ${currentVolume}, Minimum Volume: ${minimumVolume}; acceptable.")
               		    }
                   	}
					if (!(currentTrack == null)){
						//currentTrack has data
						if (!(currentTrack?.status == null)) { LOGTRACE("TALK(${appname}.${evt.name})|mP| ${it.displayName} | Current Status: ${currentStatus}, CurrentTrack: ${currentTrack}, CurrentTrack.Status: ${currentTrack.status}.") }
						if (currentTrack?.status == null) { LOGTRACE("TALK(${appname}.${evt.name})|mP| ${it.displayName} | Current Status: ${currentStatus}, CurrentTrack: ${currentTrack}.") }
						if ((currentStatus == 'playing' || currentTrack?.status == 'playing') && (!((currentTrack?.status == 'stopped') || (currentTrack?.status == 'paused')))) {  //Give currentTrack.status presidence if it exists, it seems more accurate
							if (resume) {
                            	LOGTRACE ("Sending playTrackandResume() 1")
								LOGTRACE("TALK(${appname}.${evt.name})|mP| ${it?.displayName} | cT<>null | cS/cT=playing | Sending playTrackAndResume() | CVol=${currentVolume} | SVol=${desiredVolume}")
								if (desiredVolume > -1) { 
									//if (desiredVolume == currentVolume){playTrackAndResume(it, state.sound.uri, state.sound.duration, myDelay)}
									//if (!(desiredVolume == currentVolume)){playTrackAndResume(it, state.sound.uri, state.sound.duration, desiredVolume, myDelay, phrase)}
									playTrackAndResume(it, state.sound.uri, state.sound.duration, desiredVolume, myDelay, phrase)
									spoke = true
								} else { 
									//if (currentVolume >= minimumVolume) { playTrackAndResume(it, state.sound.uri, state.sound.duration, myDelay) }
									if (currentVolume >= minimumVolume) { playTrackAndResume(it, state.sound.uri, state.sound.duration, currentVolume, myDelay, phrase) }
									if (currentVolume < minimumVolume) { playTrackAndResume(it, state.sound.uri, state.sound.duration, minimumVolume, myDelay, phrase) }
									spoke = true
								} //if (desiredVolume)
							} else {
								//resume is not desired
								LOGTRACE ("Sending playTrackandRestore() 2 - ${it?.displayName} - cVol = ${currentVolume}")
                               	LOGTRACE("TALK(${appname}.${evt.name})|mP| ${it?.displayName} | cT<>null | cS/cT=playing | NoResume! | Sending playTrackAndRestore() | CVol=${currentVolume} | SVol=${desiredVolume}")
								if (desiredVolume > -1) { 
									//if (desiredVolume == currentVolume){playTrackAndRestore(it, state.sound.uri, state.sound.duration, myDelay)}
									//if (!(desiredVolume == currentVolume)){playTrackAndRestore(it, state.sound.uri, state.sound.duration, desiredVolume, myDelay, phrase)}
									playTrackAndRestore(it, state.sound.uri, state.sound.duration, desiredVolume, myDelay, phrase)
									spoke = true
								} else { 
									//if (currentVolume >= minimumVolume) { playTrackAndRestore(it, state.sound.uri, state.sound.duration, myDelay) }
									if (currentVolume >= minimumVolume) { playTrackAndRestore(it, state.sound.uri, state.sound.duration, currentVolume, myDelay, phrase) }
									if (currentVolume < minimumVolume) { playTrackAndRestore(it, state.sound.uri, state.sound.duration, minimumVolume, myDelay, phrase) }
									spoke = true
								} // if (desiredVolume)
							} // if (resume)	
						} else {
							if (!(currentTrack?.status == 'playing') && (currentStatus == 'playing')) {
								LOGDEBUG "TALK(${appname}.${evt.name})|mP| ${it?.displayName} | Discrepency in CS/CT, going with CT! | CS= ${currentStatus} CT=${currentTrack.status}"
							}
							LOGTRACE ("Sending playTrackandRestore() 3 - to ${it?.displayName} - cVol = ${currentVolume}")
                           	LOGTRACE("TALK(${appname}.${evt.name})|mP| ${it?.displayName} | cT<>null | cS/cT<>playing | Sending playTrackAndRestore() | CVol=${currentVolume} | SVol=${desiredVolume}")
							if (desiredVolume > -1) { 
								//if (desiredVolume == currentVolume){playTrackAndRestore(it, state.sound.uri, state.sound.duration, myDelay)}
								//if (!(desiredVolume == currentVolume)){playTrackAndRestore(it, state.sound.uri, state.sound.duration, desiredVolume, myDelay, phrase)}
								playTrackAndRestore(it, state.sound.uri, state.sound.duration, desiredVolume, myDelay, phrase)
								spoke = true
							} else { 
								//if (currentVolume >= minimumVolume) { playTrackAndRestore(it, state.sound.uri, state.sound.duration, myDelay) }
								if (currentVolume >= minimumVolume) { playTrackAndRestore(it, state.sound.uri, state.sound.duration, currentVolume, myDelay, phrase) }
								if (currentVolume < minimumVolume) { playTrackAndRestore(it, state.sound.uri, state.sound.duration, minimumVolume, myDelay, phrase) }
								spoke = true
							}// if (desiredVolume)
						}// if ((currentStatus == 'playing' || currentTrack?.status == 'playing') && (!((currentTrack?.status == 'stopped') || (currentTrack?.status == 'paused'))))
					} else {
						//currentTrack==null. currentTrack doesn't have data or is not supported on this device
						if (!(currentStatus == null)) {
							LOGTRACE("TALK(${appname}.${evt.name})|mP|  ${it?.displayName} | (2) Current Status: ${currentStatus}.")
							if (currentStatus == "disconnected") {
								//VLCThing?
								if (resume) {
									LOGTRACE ("Sending playTrackandResume() 4")
                   	                LOGTRACE("TALK(${appname}.${evt.name})|mP| ${it?.displayName} | cT=null | cS=disconnected | Sending playTrackAndResume() | CVol=${currentVolume} | SVol=${desiredVolume}")
									if (desiredVolume > -1) { 
										//if (desiredVolume == currentVolume){playTrackAndResume(it, state.sound.uri, state.sound.duration, myDelay)}
										//if (!(desiredVolume == currentVolume)){playTrackAndResume(it, state.sound.uri, state.sound.duration, desiredVolume, myDelay, phrase)}
										playTrackAndResume(it, state.sound.uri, state.sound.duration, desiredVolume, myDelay, phrase)
										spoke = true
									} else { 
										//if (currentVolume >= minimumVolume) { playTrackAndResume(it, state.sound.uri, state.sound.duration, myDelay) }
										if (currentVolume >= minimumVolume) { playTrackAndResume(it, state.sound.uri, state.sound.duration, currentVolume, myDelay, phrase) }
										if (currentVolume < minimumVolume) { playTrackAndResume(it, state.sound.uri, state.sound.duration, minimumVolume, myDelay, phrase) }
										spoke = true
									}
								} else {
									//resume is not desired
                       	            LOGTRACE ("Sending playTrackandRestore() 5")
									LOGTRACE("TALK(${appname}.${evt.name})|mP| ${it?.displayName} | cT=null | cS=disconnected | No Resume! | Sending playTrackAndRestore() | CVol=${currentVolume} | SVol=${desiredVolume}")
									if (desiredVolume > -1) { 
										//if (desiredVolume == currentVolume){playTrackAndRestore(it, state.sound.uri, state.sound.duration, myDelay)}
										//if (!(desiredVolume == currentVolume)){playTrackAndRestore(it, state.sound.uri, state.sound.duration, desiredVolume, myDelay, phrase)}
										playTrackAndRestore(it, state.sound.uri, state.sound.duration, desiredVolume, myDelay, phrase)
										spoke = true
									} else { 
										//if (currentVolume >= minimumVolume) { playTrackAndRestore(it, state.sound.uri, state.sound.duration, myDelay) }
										if (currentVolume >= minimumVolume) { playTrackAndRestore(it, state.sound.uri, state.sound.duration, currentVolume, myDelay, phrase) }
										if (currentVolume < minimumVolume) { playTrackAndRestore(it, state.sound.uri, state.sound.duration, minimumVolume, myDelay, phrase) }
										spoke = true
									}// if (desiredVolume)
								}// if (resume)
							} else {
								if (currentStatus == "playing") {
									if (resume) {
                                    	LOGTRACE ("Sending playTrackandResume() 6")
										LOGTRACE("TALK(${appname}.${evt.name})|mP| ${it?.displayName} | cT=null | cS=playing | Sending playTrackAndResume() | CVol=${currentVolume} | SVol=${desiredVolume}")
										if (desiredVolume > -1) { 
											//if (desiredVolume == currentVolume){playTrackAndResume(it, state.sound.uri, state.sound.duration, myDelay)}
											//if (!(desiredVolume == currentVolume)){playTrackAndResume(it,state.sound.uri, state.sound.duration, desiredVolume, myDelay, phrase)}
											playTrackAndResume(it,state.sound.uri, state.sound.duration, desiredVolume, myDelay, phrase)
											spoke = true
										} else { 
											//if (currentVolume >= minimumVolume) { playTrackAndResume(it, state.sound.uri, state.sound.duration, myDelay) }
											if (currentVolume >= minimumVolume) { playTrackAndResume(it, state.sound.uri, state.sound.duration, currentVolume, myDelay, phrase) }
											if (currentVolume < minimumVolume) { playTrackAndResume(it, state.sound.uri, state.sound.duration, minimumVolume, myDelay, phrase) }
											spoke = true
										}// if (desiredVolume)
									} else {
										//resume not desired
										LOGTRACE ("Sending playTrackandRestore() 7")
           	                            LOGTRACE("TALK(${appname}.${evt.name})|mP| ${it?.displayName} | cT=null | cS=playing | No Resume! | Sending playTrackAndRestore() | CVol=${currentVolume} | SVol=${desiredVolume}")
										if (desiredVolume > -1) { 
											//if (desiredVolume == currentVolume){playTrackAndRestore(it, state.sound.uri, state.sound.duration, myDelay)}
											//if (!(desiredVolume == currentVolume)){playTrackAndRestore(it, state.sound.uri, state.sound.duration, desiredVolume, myDelay, phrase)}
											playTrackAndRestore(it, state.sound.uri, state.sound.duration, desiredVolume, myDelay, phrase)
											spoke = true
										} else { 
											//if (currentVolume >= minimumVolume) { playTrackAndRestore(it, state.sound.uri, state.sound.duration, myDelay) }
											if (currentVolume >= minimumVolume) { playTrackAndRestore(it, state.sound.uri, state.sound.duration, currentVolume, myDelay, phrase) }
											if (currentVolume < minimumVolume) { playTrackAndRestore(it, state.sound.uri, state.sound.duration, minimumVolume, myDelay, phrase) }
											spoke = true
										}// if (desiredVolume)
									}// if (resume)
								} else {
									//currentStatus <> "playing"
                                    LOGTRACE ("Sending playTrackandRestore() 8")
									LOGTRACE("TALK(${appname}.${evt.name})|mP| ${it?.displayName} | cT=null | cS<>playing | Sending playTrackAndRestore() | CVol=${currentVolume} | SVol=${desiredVolume}")
									if (desiredVolume > -1) { 
										//if (desiredVolume == currentVolume){playTrackAndRestore(it,state.sound.uri, state.sound.duration, myDelay)}
										//if (!(desiredVolume == currentVolume)){playTrackAndRestore(it,state.sound.uri, state.sound.duration, desiredVolume, myDelay, phrase)}
										playTrackAndRestore(it,state.sound.uri, state.sound.duration, desiredVolume, myDelay, phrase)
										spoke = true
									} else { 
										//if (currentVolume >= minimumVolume) { playTrackAndRestore(it, state.sound.uri, state.sound.duration, myDelay) }
										if (currentVolume >= minimumVolume) { playTrackAndRestore(it, state.sound.uri, state.sound.duration, currentVolume, myDelay, phrase) }
										if (currentVolume < minimumVolume) { playTrackAndRestore(it, state.sound.uri, state.sound.duration, minimumVolume, myDelay, phrase) }
										spoke = true
									}// if (desiredVolume)
								}// if (currentStatus == "playing")
							}// if (currentStatus == "disconnected"))
						} else {
							//currentTrack and currentStatus are both null
							LOGTRACE ("Sending playTrackandRestore() 9")
       	                    LOGTRACE("TALK(${appname}.${evt.name})|mP| ${it.displayName} | (3) cT=null | cS=null | Sending playTrackAndRestore() | CVol=${currentVolume} | SVol=${desiredVolume}")
							if (desiredVolume > -1) { 
                                //if (desiredVolume == currentVolume){playTrackAndRestore(it, state.sound.uri, state.sound.duration, myDelay)}
								//if (!(desiredVolume == currentVolume)){playTrackAndRestore(it, state.sound.uri, state.sound.duration, desiredVolume, myDelay, phrase)}
								playTrackAndRestore(it, state.sound.uri, state.sound.duration, desiredVolume, myDelay, phrase)
								spoke = true
							} else { 
								//if (currentVolume >= minimumVolume) { it.playTrackAndRestore(state.sound.uri, state.sound.duration, myDelay) }
								if (currentVolume >= minimumVolume) { it.playTrackAndRestore(state.sound.uri, state.sound.duration, currentVolume, myDelay, phrase) }
								if (currentVolume < minimumVolume) { it.playTrackAndRestore(state.sound.uri, state.sound.duration, minimumVolume, myDelay, phrase) }
								spoke = true
							} //if (desiredVolume)
						} //currentStatus == null
					} //currentTrack == null
				} //currentSpeechDevices.each()
			} //state.ableToTalk
		} //if (!(settings.speechDeviceDefault == null) || !(customSpeechDevice == null))
	}// if (state.speechDeviceType=="capability.musicPlayer")
	if ((mode == "capability.speechSynthesis") && (!( phrase==null ) && !(phrase==""))){
		//capability.speechSynthesis is in use
		//Prevent overlapping speech announcements by enforcing a minimum gap
		def minGapMs = 6000
		if (atomicState.lastTalkTimeS != null && (now() - atomicState.lastTalkTimeS) < minGapMs) {
			//Prevent overlapping speech announcements; wait out the gap
			pauseExecution((minGapMs - (now() - atomicState.lastTalkTimeS)) as long)
		}
		atomicState.lastTalkTimeS = now()
		if (true) {
			LOGTRACE("TALK(${appname}.${evt.name}) |sS| >> ${phrase}")
			if (!(customSpeechDevice == null)) {
				currentSpeechDevices = customSpeechDevice
			} else {
				//Use Default Speech Device
				currentSpeechDevices = getDefaultSpeechDevices()
			}// If (!(customSpeechDevice == null))
			//Iterate Speech Devices and talk
			def attrs = currentSpeechDevices.supportedAttributes
			currentSpeechDevices.each(){
				// Determine device name either by it.displayName or it.device.displayName (whichever works)
				LOGDEBUG("FINGERPRINT ${it.displayName} cap:${it.getSupportedCapabilities} comm:${it.getSupportedCommands()} att:${it.getSupportedAttributes()}")
				try {
					LOGTRACE("TALK(${appname}.${evt.name}) |sS| ${it.displayName} | Sending speak().")
				}
				catch (ex) {
					LOGDEBUG("TALK(${appname}.${evt.name}) |sS| it.displayName failed, trying it.device.displayName")
					try {
						LOGTRACE("TALK(${appname}.${evt.name}) |sS| ${it.device.displayName} | Sending speak().")
					}
					catch (ex2) {
						LOGDEBUG("TALK(${appname}.${evt.name}) |sS| it.device.displayName failed, trying it.device.name")
						LOGTRACE("TALK(${appname}.${evt.name}) |sS| ${it.device.name} | Sending speak().")
					}
				}
				spoke = true
				if (it.hasCommand("speak")) {
				it.speak(phrase)
			} else {
				LOGERROR("TALK(${appname}.${evt.name}) |sS| ${it.displayName} does not support speak() - skipped.")
			}
		}// currentSpeechDevices.each()
		} //if (!(settings.speechDeviceDefault == null) || !(customSpeechDevice == null))
	} //if (mode == "capability.speechSynthesis")

	if ((mode == "capability.audioNotification") && (!( phrase==null ) && !(phrase==""))){
		//capability.audioNotification is in use
		//Prevent overlapping audio notifications by enforcing a minimum gap
		def minGapMs = 6000
		if (atomicState.lastTalkTimeA != null && (now() - atomicState.lastTalkTimeA) < minGapMs) {
			//Prevent overlapping audio notifications; wait out the gap
			pauseExecution((minGapMs - (now() - atomicState.lastTalkTimeA)) as long)
		}
		atomicState.lastTalkTimeA = now()
		if (true) {
			LOGTRACE("TALK(${appname}.${evt.name}) |aN| >> ${phrase}")
			if (!(customSpeechDevice == null)) {
				currentSpeechDevices = customSpeechDevice
			} else {
				currentSpeechDevices = getDefaultSpeechDevices()
			}
			state.sound = null
			state.ableToTalk = false
			if (!(phrase.toLowerCase().contains(".mp3"))){
				try {
					state.sound = textToSpeech(phrase instanceof List ? phrase[0] : phrase, myVoice)
					state.ableToTalk = true
				} catch(e) {
					LOGERROR("TALK(${appname}.${evt.name})|aN| Platform issue (textToSpeech)? ${e}")
					try {
						def fallbackVoice = (settings?.speechVoice ?: "Salli(en-us)").replaceAll(/\(.*\)/, '')
						state.sound = textToSpeech(phrase instanceof List ? phrase[0] : phrase, fallbackVoice)
						state.ableToTalk = true
					} catch(ex) {
						LOGERROR("TALK(${appname}.${evt.name})|aN| textToSpeech() failed twice. Giving up.")
					}
				}
			} else {
				if (isSafeUrl(phrase)) {
					state.sound = [uri: phrase, duration: 10]
					state.ableToTalk = true
				} else {
					LOGERROR("TALK(${appname}.${evt.name})|aN| Rejected unsafe MP3 URL: ${phrase}")
				}
			}
			if (state.ableToTalk) {
				def desiredVolume = getDesiredVolume(volume)
				if (desiredVolume > 100) {desiredVolume = 100}
				if (desiredVolume < 0) {desiredVolume = 50}
				def soundUri = state.sound.uri
				for (dev in currentSpeechDevices) {
					if (dev.hasCommand("playTrackAndRestore")) {
						try { dev.playTrackAndRestore(soundUri, desiredVolume); spoke = true } catch (ex) { LOGERROR("TALK(${appname}.${evt.name})|aN| ${dev.displayName} playTrackAndRestore failed: ${ex}") }
					} else if (dev.hasCommand("playTrack")) {
						try { dev.playTrack(soundUri, desiredVolume); spoke = true } catch (ex) { LOGERROR("TALK(${appname}.${evt.name})|aN| ${dev.displayName} playTrack failed: ${ex}") }
					} else {
						LOGERROR("TALK(${appname}.${evt.name})|aN| ${dev.displayName} does not support playTrack or playTrackAndRestore")
					}
				}
			}
		}
	} //if (mode == "capability.audioNotification")

	if ((!(smartAppSpeechDevice) && !(spoke)) && (!(phrase == null) && !(phrase == "")) && !(playAudioFile)) {
		//No musicPlayer, speechSynthesis, or smartAppSpeechDevices selected. No route to export speech!
		LOGTRACE("TALK(${appname}.${evt.name}) |ERROR| No selected speech device or smartAppSpeechDevice token in phrase. ${phrase}")
	} else {
    	if ((smartAppSpeechDevice && !spoke) && (!(phrase == null) && !(phrase == ""))){
			LOGTRACE("TALK(${appname}.${evt.name}) |sA| Sent to another smartApp.")
       	}
   	}
    phrase = ""
}//Talk()


def getTimeFromDateString(inputtime, includeAmPm){
    //I couldn't find the way to do this in ST / Groovy, so I made my own function
    //Obtains the time from a supplied specifically formatted date string (ie: from a preference of type "time")
    //LOGDEBUG "InputTime: ${inputtime}"
    def outputtime = inputtime
    def am_pm = "??"
    outputtime = inputtime.substring(11,16)
    if (includeAmPm) {
        if ((outputtime.substring(0,2)).toInteger() < 12) { 
            am_pm = "am" 
        } else { 
            am_pm = "pm"
            def newHH = ((outputtime.substring(0,2)).toInteger() - 12)
            outputtime = newHH + outputtime.substring(2,5)
        }
        outputtime += am_pm
    }
    //LOGDEBUG "OutputTime: ${outputtime}"
    return outputtime
}

def getTimeFromCalendar(includeSeconds, includeAmPm){
    //Obtains the current time:  HH:mm:ss am/pm
    def calendar = Calendar.getInstance()
	calendar.setTimeZone(location.timeZone)
	def timeHH = calendar.get(Calendar.HOUR)
    def timemm = calendar.get(Calendar.MINUTE)
    def timess = calendar.get(Calendar.SECOND)
    def timeampm = calendar.get(Calendar.AM_PM) ? "pm" : "am"
    def timestring = "${timeHH}:${timemm}"
    if (includeSeconds) { timestring += ":${timess}" }
    if (includeAmPm) { timestring += " ${timeampm}" }
    return timestring
}

//myRunIn from ST:Geko / Statusbits SmartAlarm app http://statusbits.github.io/smartalarm/
private def myRunIn(delay_s, func) {
    //LOGDEBUG("myRunIn(${delay_s},${func})")

    if (delay_s > 0) {
        def tms = now() + (delay_s * 1000)
        def date = new Date(tms)
        runOnce(date, func)
        //LOGDEBUG("'${func}' scheduled to run at ${date}")
    }
}

def TalkQueue(appname, phrase, customSpeechDevice, volume, resume, personality, voice, evt){
    //IN DEVELOPMENT
    // Already talking or just recently (within x seconds) started talking
    // Queue up current request(s), give time for current action to complete, then speak and flush queue
    def threshold = 0
    def minDelay = 6 //Minimum seconds between talking
    try {
    	if (!(state?.sound?.duration == null)) { 
    		threshold = state.sound.duration.toInteger() //Use the last musicPlayer sound duration from the last Talk call as the minimum delay
    	}
    } catch (exception) {
    	threshold = 10
    }
    def durationFromLastTalkReq = 9999
    //if (!(state.lastTalkTime == null)) { durationFromLastTalk = ((now() - state?.lastTalkTime)/1000).intValue() }
    if (!(atomicState.lastTalkRequest == null)) { durationFromLastTalkReq = ((now() - atomicState.lastTalkRequest)/1000).intValue() }
    atomicState.lastTalkRequest = now()
    def tooSoon = (durationFromLastTalkReq < threshold)
    def neededDelay = (((threshold - durationFromLastTalkReq) * 1000) + 1000)
    LOGDEBUG ("TALKQUEUE(Threshold=${threshold},DurationFromLastTalkReq=${durationFromLastTalkReq},lastTalkReq=${atomicState.lastTalkRequest},lastTalkTime=${atomicState.lastTalkTime}, TooSoon=${tooSoon}, Calc=${neededDelay}")
    if (tooSoon) {
    	if (neededDelay < 0) { 
        	neededDelay = 0 
        } else {
    		if (neededDelay < (minDelay * 1000)) { neededDelay = (minDelay * 1000) }
        }
    	LOGDEBUG("TALKQUEUE()-Spoke too recently; delaying ${(neededDelay / 1000)} seconds.")
        return neededDelay
    } else {
    	LOGDEBUG("TALKQUEUE()-OK to speak; (${(durationFromLastTalkReq)})")
    	return 0
    }
}

def getWeather(mode, zipCode) {
	//Uses the free, global, key-less Open-Meteo API (works US/UK/worldwide)
	def msg = ""
	//Use the cached forecast if it's fresh (<10 min)
	if (state?.weatherCacheTime != null && state?.weatherCacheMode == mode && (now() - state.weatherCacheTime) < 600000 && state?.weatherCacheMsg) {
		LOGDEBUG("getWeather: returning cached msg for mode=${mode}")
		return state.weatherCacheMsg
	}
	if ((state?.latitude == null || state?.latitude == "") && settings?.latitude == null) { state.latitude = "${location.latitude}" }
	if ((state?.longitude == null || state?.longitude == "") && settings?.longitude == null) { state.longitude = "${location.longitude}" }
	def glat = null
	def glon = null
	try { glat = state.latitude?.toString()?.toDouble() } catch (ex) {}
	try { glon = state.longitude?.toString()?.toDouble() } catch (ex) {}
	if (glat == null || glon == null || glat < -90 || glat > 90 || glon < -180 || glon > 180) {
		return "The weather is currently unavailable."
	}
	state.latitude = glat
	state.longitude = glon
	def tempUnit = (location.temperatureScale == "C") ? "celsius" : "fahrenheit"
	def wxUri = "https://api.open-meteo.com/v1/forecast?latitude=${state.latitude}&longitude=${state.longitude}&current_weather=true&daily=weathercode,temperature_2m_max,temperature_2m_min&timezone=auto&forecast_days=2&temperature_unit=${tempUnit}"
	LOGTRACE("DEBUG wxURI: ${wxUri}")
	def requestParams = [
		uri:  wxUri,
		requestContentType: "application/json",
		contentType: "application/json"
	]
	try {
		httpGet(requestParams) { response ->
			if (response?.status == 200) {
				LOGDEBUG("getWeather: response=${response.data}")
				def cw = response.data?.current_weather
				def daily = response.data?.daily
				def todayHigh = daily?.temperature_2m_max ? daily.temperature_2m_max[0] : null
				def todayLow = daily?.temperature_2m_min ? daily.temperature_2m_min[0] : null
				def tomorrowHigh = (daily?.temperature_2m_max && daily.temperature_2m_max.size() > 1) ? daily.temperature_2m_max[1] : null
				def tomorrowLow = (daily?.temperature_2m_min && daily.temperature_2m_min.size() > 1) ? daily.temperature_2m_min[1] : null
				def todayCode = daily?.weathercode ? daily.weathercode[0] : (cw?.weathercode ?: null)
				def tomorrowCode = (daily?.weathercode && daily.weathercode.size() > 1) ? daily.weathercode[1] : null
				def tUnit = tempUnit == "celsius" ? "celsius" : "fahrenheit"
				if (mode == "current") {
					msg = "The current temperature is ${cw?.temperature} degrees ${tUnit} with ${weatherCodeToText(cw?.weathercode)}."
				}
				if (mode == "today") {
					msg = "Today the forecast is ${weatherCodeToText(todayCode)} with a high of ${todayHigh} and a low of ${todayLow} degrees ${tUnit}."
				}
				if (mode == "tonight") {
					msg = "Tonight the forecast is ${weatherCodeToText(todayCode)} with a low of ${todayLow} degrees ${tUnit}."
				}
				if (mode == "tomorrow") {
					msg = "Tomorrow the forecast is ${weatherCodeToText(tomorrowCode)} with a high of ${tomorrowHigh} and a low of ${tomorrowLow} degrees ${tUnit}."
				}
			}
		}
	} catch (ex) {
		LOGERROR("getWeather(): ${ex}")
	}
	if (msg == "" || msg == null) {
		if (state?.weatherCacheMsg) { msg = state.weatherCacheMsg } else { msg = "The weather is currently unavailable." }
	} else {
		state.weatherCacheMsg = msg
		state.weatherCacheTime = now()
		state.weatherCacheMode = mode
	}
	return msg
}

//Only allow plain http/https URLs (prevents file://, javascript:, data: injection),
//and only allow plain http for local/trusted hosts.
def isSafeUrl(url) {
    if (url == null) { return false }
	def match = url.toString().trim() =~ /(?i)^(https?):\/\/([^\/?#]+)(?:[\/?#].*)?$/
	if (!match.matches()) { return false }
	def protocol = match[0][1].toLowerCase()
	def authority = match[0][2]
	if (authority.contains('@') || authority.any { it <= ' ' }) { return false }

	def host = authority
	def port = null
	if (authority.startsWith('[')) {
		def closingBracket = authority.indexOf(']')
		if (closingBracket < 0) { return false }
		host = authority.substring(1, closingBracket).toLowerCase()
		def remainder = authority.substring(closingBracket + 1)
		if (remainder != '') {
			if (!(remainder ==~ /:[0-9]{1,5}/)) { return false }
			port = remainder.substring(1).toInteger()
		}
		if (!(host ==~ /(?i)[0-9a-f:.]+/)) { return false }
	} else {
		def colon = authority.lastIndexOf(':')
		if (colon >= 0) {
			if (authority.indexOf(':') != colon) { return false }
			def portText = authority.substring(colon + 1)
			if (!(portText ==~ /[0-9]{1,5}/)) { return false }
			port = portText.toInteger()
			host = authority.substring(0, colon)
		}
		host = host.toLowerCase()
		if (!(host ==~ /[a-z0-9.-]+/)) { return false }
	}
	if (port != null && (port < 1 || port > 65535)) { return false }
	if (protocol == 'https') { return true }
	if (host == 'localhost' || host == '::1' || host.endsWith('.local')) { return true }
	if (host ==~ /[0-9]{1,3}(\.[0-9]{1,3}){3}/) {
		def octets = host.split('\\.').collect { it.toInteger() }
		if (octets.any { it > 255 }) { return false }
		return octets[0] == 10 || octets[0] == 127 ||
			(octets[0] == 172 && octets[1] >= 16 && octets[1] <= 31) ||
			(octets[0] == 192 && octets[1] == 168) ||
			(octets[0] == 169 && octets[1] == 254)
	}
	return host ==~ /(?i)fe[89ab][0-9a-f]*:.*/ || host ==~ /(?i)fec[0-3]:.*/
}

def weatherCodeToText(code) {
	//WMO weather code -> plain text for speech
	if (code == null) { return "unknown conditions" }
	def m = [
		0: "clear sky",
		1: "mainly clear",
		2: "partly cloudy",
		3: "overcast",
		45: "fog",
		48: "depositing rime fog",
		51: "light drizzle",
		53: "moderate drizzle",
		55: "dense drizzle",
		56: "light freezing drizzle",
		57: "dense freezing drizzle",
		61: "slight rain",
		63: "moderate rain",
		65: "heavy rain",
		66: "light freezing rain",
		67: "heavy freezing rain",
		71: "slight snowfall",
		73: "moderate snowfall",
		75: "heavy snowfall",
		77: "snow grains",
		80: "slight rain showers",
		81: "moderate rain showers",
		82: "violent rain showers",
		85: "slight snow showers",
		86: "heavy snow showers",
		95: "thunderstorm",
		96: "thunderstorm with slight hail",
		99: "thunderstorm with heavy hail"
	]
	return m[code as Integer] ?: "unknown conditions"
}

def poll(){
    if (settings?.resumePlay == true || settings?.resumePlay == null) {
		unschedule("poll")
    	//LOGDEBUG("poll() settings=${settings?.allowScheduledPoll}")
    	//LOGDEBUG("poll() state=${state?.allowScheduledPoll}")
    	//LOGDEBUG("poll() resumePlay=${settings?.resumePlay}")
    	if (settings?.allowScheduledPoll != false) {
	    	state.allowScheduledPoll = true
    	} else {
    		state.allowScheduledPoll = false
        	LOGDEBUG("Polling is not desired, disabling after this poll.")
    	}
    	if (true) {
        	LOGDEBUG("Polling speech device(s) for latest status")
        	state.polledDevices = ""
        	try {
            	if (!(settings?.speechDeviceDefaultMusicPlayer == null)) {dopoll(settings.speechDeviceDefaultMusicPlayer)}
        	} catch(e) {
	            LOGERROR("One of your speech devices is not responding.  Poll failed.")
                LOGDEBUG("BT_poll() Error: ${e}")
    	    }
        	state.lastPoll = getTimeFromCalendar(true,true)
        	//LOGDEBUG("poll: state.polledDevices == ${state?.polledDevices}")
        	if (!(state?.polledDevices == "")) {
	        	//Reschedule next poll
    	    	if (settings?.allowScheduledPoll != false) {
        	    	LOGDEBUG("Rescheduling Poll")
            	    myRunIn(60, poll) 
            	}
        	} else {
        		LOGDEBUG("No speech devices polled. Cancelling polling.")
        	}
    	}
	}
}
def dopoll(pollSpeechDevice){
    pollSpeechDevice.each(){
    	def devicename = ""
        try {
        	devicename = it.displayName
        } catch (ex) {}
        if (devicename == "") {
        	try {
            	devicename = it.device.displayName
            } catch (ex) {}
        }
        if (devicename == "") {
        	LOGERROR("dopoll(${pollSpeechDevice}) - Unable to get devicename")
        }
        if (!(state?.polledDevices?.find("|${devicename}|"))) {
            state.polledDevices = state?.polledDevices + "|${devicename}|"
            LOGDEBUG("dopoll(${devicename}) Polling ")
            state.refresh = false
            state.poll = false
            try {
                //LOGTRACE("refresh()")
                it.refresh()
                state.refresh = true
            }
            catch (ex) {
                LOGDEBUG("ERROR(informational): it.refresh: ${ex}")
                state.refresh = false
            }
            //LOGDEBUG("dopoll(${devicename}) after refresh() ")
            if (!(state.refresh)) {
                try {
                    //LOGTRACE("poll()")
                    it.poll()
                    state.poll = true
                    state.refresh = true
                }
                catch (ex) {
                    LOGDEBUG ("ERROR(informational): it.poll: ${ex}")
                    state.poll = false
                    state.refresh = false
                }
            }
            //LOGDEBUG("dopoll(${devicename}) after poll()")
            //LOGDEBUG("dopoll(${devicename}) refresh=${state.refresh} poll=${state.poll}")
    	    //LOGDEBUG("dopoll(${it.displayName})cS=${it?.latestValue('status')},cT=${it?.latestState("trackData")?.jsonValue?.status},cV=${it?.latestState("level")?.integerValue ? it?.latestState("level")?.integerValue : 0}")
            if (it?.latestValue('status') == "no_device_present") { LOGTRACE("During polling, the handler for ${devicename} indicated the device was not found.") } //VLCThing
        }
        LOGDEBUG("dopoll - polled devices: ${state?.polledDevices}")
    }
}

def getSharedLastTalkMs() {
    //Shared across all BigTalker child groups via a location event
    try { return location.currentState("BigTalkerLastTalk")?.value?.toString()?.toLong() } catch (ex) { return null }
}

def noteSharedLastTalk() {
    try { sendLocationEvent(name: "BigTalkerLastTalk", value: "${now()}") } catch (ex) {}
}

def replaceTokenIgnoreCase(String phrase, String needle, String replacement) {
    if (phrase == null) { return phrase }
    def needleRegex = needle.replaceAll(/[.\[\]{}()*+?^$\\|]/, '\\$0')
    return phrase.replaceAll(/(?i)${needleRegex}/, java.util.regex.Matcher.quoteReplacement(replacement?.toString() ?: ''))
}

def getDefaultSpeechDevices(){
    //Default devices picked in the app defaults page, all capability types
    def devs = []
    if (settings?.speechDeviceDefaultMusicPlayer) { devs = devs + settings.speechDeviceDefaultMusicPlayer }
    if (settings?.speechDeviceDefaultSpeechSynthesis) { devs = devs + settings.speechDeviceDefaultSpeechSynthesis }
    if (settings?.speechDeviceDefaultAudioNotification) { devs = devs + settings.speechDeviceDefaultAudioNotification }
    return devs
}

def getDesiredVolume(invol) {
	def globalVolume = settings?.speechVolume
    def globalMinimumVolume = settings?.speechMinimumVolume
    def myVolume = invol
    def finalVolume = -1
    if (myVolume > 0) { 
    	finalVolume = myVolume
	} else {
		if (globalVolume > 0) {
			finalVolume = globalVolume
		} else {
            if (globalMinimumVolume > 0) {
                finalVolume = globalMinimumVolume
            } else {
                finalVolume = 50 //Default if no volume parameters are set
            }
        }
	}
    if (true) { 
    	LOGDEBUG("finalVolume: ${finalVolume}")
    }
    return finalVolume
}

def setMode(mode){
  // Remove this function
}

def onModeChangeEvent(evt){
	state.lastMode = state.mode
	state.mode = location.mode
	LOGDEBUG("LastMode=${state.lastMode} Mode=${location.mode}")
}

def disableDebug(){
	LOGTRACE("Debug timer has expired. Disabling debugging")
	state.debugMode = false
	state.debugAutoOff = true
	unschedule("disableDebug")
}

def LOGDEBUG(txt){
	if (state.debugMode) { 
		def msgfrom = "[PARENT] "
		def appLabel = (app?.label == null) ? state.InternalName : app.label //Some child calls to parent.LOGDEBUG result in app.label being null, correct
		appLabel = appLabel.replace(" ","")
		appLabel.toUpperCase()
		if (txt?.contains("[CHILD:")) { msgfrom = "" }
		txt = txt?.toString()?.replaceAll(/[\r\n]+/, ' ')
    	try {
    		log.debug("${appLabel}(${state.version}) || ${msgfrom}${txt}")
    	} catch(ex) {
			log.error("LOGDEBUG unable to output requested data! || err:${ex}")
    	}
	}
}
def LOGTRACE(txt){
	def msgfrom = "[PARENT] "
	def appLabel = (app?.label == null) ? state.InternalName : app.label //Some child calls to parent.LOGTRACE result in app.label being null, correct
	appLabel = appLabel.replace(" ","")
	appLabel.toUpperCase()
    if (txt?.contains("[CHILD:")) { msgfrom = "" }
	    txt = txt?.toString()?.replaceAll(/[\r\n]+/, ' ')
    try {
    	log.trace("${appLabel}(${state.version}) || ${msgfrom}${txt}")
    } catch(ex) {
    	log.error("LOGTRACE unable to output requested data!")
    }
}
def LOGERROR(txt){
	def msgfrom = "[PARENT] "
	def appLabel = (app?.label == null) ? state.InternalName : app.label //Some child calls to parent.LOGERROR result in app.label being null, correct
	appLabel = appLabel.replace(" ","")
	appLabel.toUpperCase()
    if (txt?.contains("[CHILD:")) { msgfrom = "" }
	    txt = txt?.toString()?.replaceAll(/[\r\n]+/, ' ')
    try {
    log.error("${appLabel}(${state.version}) || ${msgfrom}ERROR: ${txt}")
    } catch(ex) {
    	log.error("LOGERROR unable to output requested data!")
    }
}

def getHubType(){
    return "Hubitat"
}

//Escape user/remote-supplied text for safe rendering inside HTML
def escapeHtml(sText) {
	if (sText == null) { return "" }
	def s = sText.toString()
	s = s.replaceAll(/&/, '&amp;')
	s = s.replaceAll(/</, '&lt;')
	s = s.replaceAll(/>/, '&gt;')
	s = s.replaceAll(/"/, '&quot;')
	s = s.replaceAll(/'/, '&#39;')
	return s
}

def returnVar(var) {
    def dataType = "String"
    def returnValue
    if (!(settings."${var}" == null)) { returnValue = settings."${var}" }
    if (!(state."${var}" == null)) { returnValue = state."${var}" }
	if (!(atomicState."${var}" == null)) { returnValue = atomicState."${var}" }
    def dateTest = returnValue =~ /\d\d\d\d-\d\d-\d\dT\d\d:/
    if (dateTest) { dataType = "Date" }
    if (returnValue == "true") { dataType = "Boolean" }
    if (returnValue == "false") { dataType = "Boolean" }
    if (returnValue == true) { dataType = "Boolean" }
    if (returnValue == false) { dataType = "Boolean" }
    if (dataType == "Date") {returnValue = Date.parse("yyyy-MM-dd'T'HH:mm:ss.SSSZ", returnValue)}
    //LOGDEBUG ("returnVar(${var}), DataType:${dataType}, Value: ${returnValue}")
    if (returnValue == null || returnValue == "") {}
    return returnValue
}

def playTrackAndRestore(device, uri, duration, volume, myDelay, phrase) {
    LOGDEBUG("playTrackAndRestore(${device.displayName},${uri},${duration},${volume},${myDelay})")
    if (state.hubType == "Hubitat") {
		def isEchoSpeaksLike = device.hasCommand("setVolumeAndSpeak")
		def isSonosLike = device.hasCommand("playTrackAndRestore") //Hubitat only supports playTrackAndRestore(uri,volume) for Sonos
		def supportsPlayTrack = device.hasCommand("playTrack")
		def supportsSetLevel = device.hasCommand("setLevel")
		def supportsSetVolume = device.hasCommand("setVolume")
		def supportsUnmute = device.hasCommand("unmute")
		def supportsATTRLevel = device.hasAttribute("level")
		def supportsATTRVolume = device.hasAttribute("volume")
		def curVol = -100
		if (supportsATTRLevel){ curVol = device.latestValue("level")?.toString()?.toInteger() ?: -100 }
		if (supportsATTRVolume){ curVol = device.latestValue("volume")?.toString()?.toInteger() ?: -100 }
		LOGDEBUG("FINGERPRINT ${device.displayName} cap:${device.getSupportedCapabilities} comm:${device.getSupportedCommands()} att:${device.getSupportedAttributes()}")
		if (isSonosLike){ //NEED ADDITIONAL SUPPORTED COMMANDS/ATTRIBUTES TO FINGERPRINT!
			// SONOS SUPPORT
			LOGDEBUG("playTrackAndRestore: Sonos like device detected, Please send previous debug log entry to Rayzurbock for Fingerprinting.")
			//LOGDEBUG("playTrackAndRestore: Sonos like device detected, calling playTrackAndRestore(uri,vol)")
			//device.playTrackAndRestore(uri, volume)  //Hubitat only supports playTrackAndRestore(uri,volume) for Sonos
			//return
		}
		if (isEchoSpeaksLike) {
			// ECHOSPEAKS SUPPORT
			// setVolumeSpeakAndRestore - Sets volume, plays message, restores volume
			LOGDEBUG("playTrackAndRestore: EchoSpeaks like device detected, calling setVolumeSpeakAndRestore(volume, message)")
			setVolumeSpeakAndRestore(volume, phrase)
			return
		}
		if (supportsPlayTrack) {
			def curTrack = device.trackData
			if (supportsUnmute){
				LOGDEBUG("playTrackAndRestore[playTrack]: unmute()")
				device.unmute()
				pauseExecution(250)
			} else {
				LOGDEBUG("playTrackAndRestore[playTrack]: unmute() is not supported by this device (${device.displayName})")
			}
			if (supportsSetLevel || supportsSetVolume){
				if (supportsSetLevel){
					LOGDEBUG("playTrackAndRestore[playTrack]: setLevel(${volume})")
					device.setLevel(volume)
					pauseExecution(250)
				}
				if (supportsSetVolume && !supportsSetLevel){
					LOGDEBUG("playTrackAndRestore[playTrack]: setVolume(${volume})")
					device.setVolume(volume)
					pauseExecution(250)
				}
			} else {
				LOGDEBUG("playTrackAndRestore[playTrack]: setLevel/setVolume is not supported by this device (${device.displayName})")
			}
			LOGDEBUG("playTrackAndRestore[playTrack]: playTrack(${uri})")
			device.playTrack(uri)
			pauseExecution((duration.toInteger() * 1000))
			if (supportsSetLevel || supportsSetVolume){
				if ((curVol > -100) && supportsSetLevel){
					LOGDEBUG("playTrackAndRestore[playTrack]: setLevel(${curVol})")
					device.setLevel(curVol)
					pauseExecution(250)
				}
				if ((curVol > -100) && (supportsSetVolume && !supportsSetLevel)){
					LOGDEBUG("playTrackAndRestore[playTrack]: setVolume(${curVol})")
					device.setVolume(curVol)
					pauseExecution(250)
				}
			} else {
				LOGDEBUG("playTrackAndRestore[playTrack]: setLevel/setVolume is not supported by this device (${device.displayName})")
			}
			if (!curTrack?.uri == null){
				LOGDEBUG("playTrackAndRestore[playTrack]: restoreTrack(${curTrack.uri})")
				if (device.hasCommand("restoreTrack")){
					device.restoreTrack(curTrack.uri)
				} else {
					LOGDEBUG("playTrackAndRestore[playTrack]: restoreTrack() is not supported by this device (${device.displayName})")
				}
			}
		}
    }
}

def playTrackAndResume(device, uri, duration, volume, myDelay, phrase) {
    LOGDEBUG("playTrackAndResume(${device.displayName},${uri},${duration},${volume},${myDelay})")
    if (state.hubType == "Hubitat") {
		def isEchoSpeaksLike = device.hasCommand("setVolumeAndSpeak")
		def isSonosLike = device.hasCommand("playTrackAndRestore") //Hubitat only supports playTrackAndRestore(uri,volume) for Sonos
		def supportsPlayTrack = device.hasCommand("playTrack")
		def supportsSetLevel = device.hasCommand("setLevel")
		def supportsSetVolume = device.hasCommand("setVolume")
		def supportsUnmute = device.hasCommand("unmute")
		def supportsATTRLevel = device.hasAttribute("level")
		def supportsATTRVolume = device.hasAttribute("volume")
		def curVol = -100
		if (supportsATTRLevel){ curVol = device.latestValue("level")?.toString()?.toInteger() ?: -100 }
		if (supportsATTRVolume){ curVol = device.latestValue("volume")?.toString()?.toInteger() ?: -100 }
		LOGDEBUG("FINGERPRINT ${device.displayName} cap:${device.getSupportedCapabilities} comm:${device.getSupportedCommands()} att:${device.getSupportedAttributes()}")
		if (isSonosLike){ //NEED ADDITIONAL SUPPORTED COMMANDS/ATTRIBUTES TO FINGERPRINT!
			// SONOS SUPPORT
			LOGDEBUG("playTrackAndRestore: Sonos like device detected, Please send previous debug log entry to Rayzurbock for Fingerprinting.")
			//LOGDEBUG("playTrackAndRestore: Sonos like device detected, calling playTrackAndRestore(uri,vol)")
			//device.playTrackAndRestore(uri, volume)  //Hubitat only supports playTrackAndRestore(uri,volume) for Sonos
			//return
		}
		if (isEchoSpeaksLike) {
			// ECHOSPEAKS SUPPORT
			// setVolumeSpeakAndRestore - Sets volume, plays message, restores volume
			LOGDEBUG("playTrackAndResume: EchoSpeaks like device detected, calling setVolumeSpeakAndRestore(volume, message); Resume track not supported")
			setVolumeSpeakAndRestore(volume, phrase)
			return
		}
		if (supportsPlayTrack) {
			def curTrack = device.trackData
			if (supportsUnmute){
				LOGDEBUG("playTrackAndResume[playTrack]: unmute()")
				device.unmute()
				pauseExecution(250)
			} else {
				LOGDEBUG("playTrackAndResume[playTrack]: unmute() is not supported by this device (${device.displayName})")
			}
			if (supportsSetLevel || supportsSetVolume){
				if (supportsSetLevel){
					LOGDEBUG("playTrackAndResume[playTrack]: setLevel(${volume})")
					device.setLevel(volume)
					pauseExecution(250)
				}
				if (supportsSetVolume && !supportsSetLevel){
					LOGDEBUG("playTrackAndResume[playTrack]: setVolume(${volume})")
					device.setVolume(volume)
					pauseExecution(250)
				}
			} else {
				LOGDEBUG("playTrackAndResume[playTrack]: setLevel/setVolume is not supported by this device (${device.displayName})")
			}
			LOGDEBUG("playTrackAndResume[playTrack]: playTrack(${uri})")
			device.playTrack(uri)
			pauseExecution((duration.toInteger() * 1000))
			if (supportsSetLevel || supportsSetVolume){
				if ((curVol > -100) && supportsSetLevel){
					LOGDEBUG("playTrackAndResume[playTrack]: setLevel(${curVol})")
					device.setLevel(curVol)
					pauseExecution(250)
				}
				if ((curVol > -100) && (supportsSetVolume && !supportsSetLevel)){
					LOGDEBUG("playTrackAndResume[playTrack]: setVolume(${curVol})")
					device.setVolume(volume)
					pauseExecution(250)
				}
			} else {
				LOGDEBUG("playTrackAndResume[playTrack]: setLevel/setVolume is not supported by this device (${device.displayName})")
			}
			if (!curTrack?.uri == null){
				LOGDEBUG("playTrackAndResume[playTrack]: resumeTrack(${curTrack.uri})")
				if (device.hasCommand("resumeTrack")){
					device.resumeTrack(curTrack.uri)
				} else {
					LOGDEBUG("playTrackAndRestore[playTrack]: restoreTrack() is not supported by this device (${device.displayName})")
					if (device.hasCommand("restoreTrack")){
						device.restoreTrack(curTrack.uri)
					}
				}
			}
		}
    }
}

def version(){
	//Cobra update code, modified by Rayzurbock
    resetBtnName()
	//schedule("0 0 9 ? * FRI *", updateCheck) //  Check for updates at 9am every Friday
	updateCheck()  
	//checkButtons()
    //pauseOrNot()
    
}

def checkButtons(){
	//Cobra update code
    LOGDEBUG("Running checkButtons")
    appButtonHandler("updateBtn")
}


def appButtonHandler(btn){
	//Cobra update code
    state.btnCall = btn
    if(state.btnCall == "updateBtn"){
       log.info "Checking for updates now..."
        updateCheck()
        pause(3000)
  		state.btnName = state.versionBtn
        runIn(2, resetBtnName)
    }
    if(state.btnCall == "updateBtn1"){
    state.btnName = "Update Available - refresh page" 
    //httpGet("https://github.com/CobraVmax/Hubitat/tree/master/Apps' target='_blank")
    }
    
}   
def resetBtnName(){
	//Cobra update code
//    log.info "Resetting Update Button Name"
	if(state.versionStatus != "Current"){
		state.btnName = state.versionBtn
	}
	else{
		state.btnName = "Check For Update" 
	}
}    

def displayVersionStatus(){
	//Cobra update code, modified by Rayzurbock
    def formatSettingRootStart = state.formatSettingRootStart
	def formatSettingRootEnd = state.formatSettingRootEnd
	def formatSettingOptionalStart = state.formatSettingOptionalStart
	def formatSettingOptionalEnd = state.formatSettingOptionalEnd
    def formatUlStart = state.formatUlStart
	def formatUlEnd = state.formatUlEnd
    def formatLiStart = state.formatLiStart
	def formatLiEnd = state.formatLiEnd
    def formatIStart = state.formatIStart
	def formatIEnd = state.formatIEnd
    def formatStrongStart = state.formatStrongStart
	def formatStrongEnd = state.formatStrongEnd
    def formatHr = state.formatHr
    def formatBr = state.formatBr
    def formatCenterStart = state.formatCenterStart
    def formatCenterEnd = state.formatCenterEnd
    def versionInfo = ""
	versionInfo = "${formatHr}${formatHr}${formatStrongStart}${formatCenterStart}Version Information${formatCenterEnd}${formatStrongEnd}${formatBr}"
	if(state.versionStatus){
    	if (state.hubType == "Hubitat") {
			versionInfo += "<img src='https://lowrance.cc/ST/icons/BigTalker-CurrentVersion.png'</img><BR>${state.ExternalName} - Version: ${state.version} <BR><font face='Lucida Handwriting'>${state.Copyright ?: ""} </font>"
		}
    }
	if((state.versionStatus != "${formatStrongStart}** This app is no longer supported by $state.author  **${formatStrongEnd}") & (state.versionStatus != "Current")){
		input "updateBtn", "button", title: "${state.btnName}"
    
		//  section(){
		//		log.info "app.label = $app.label"
		//		input "pause1", "bool", title: "Pause This App", required: true, submitOnChange: true, defaultValue: false  
		//	}
		//	pauseOrNot()   
			//if(state.versionStatus != "Current"){
		versionInfo += "${formatStrongStart}${escapeHtml(state.versionStatus ?: "")}${formatStrongEnd}${formatBr}${escapeHtml(state.updateURI ?: "")}${formatBr}${formatStrongStart}${formatIStart}Release Notes:${formatIEnd}${formatStrongEnd}${escapeHtml(state.UpdateInfo ?: "")}${formatBr}"
			//}
		//section(" ") {
		//	input "updateNotification", "bool", title: "Send a 'Pushover' message when an update is available", required: true, defaultValue: false, submitOnChange: true 
		//	if(updateNotification == true){ input "speaker", "capability.speechSynthesis", title: "PushOver Device", required: true, multiple: true}
		//}
	} else {
		versionInfo += "${formatIStart}App is up to date${formatIEnd}"
	}
    paragraph versionInfo
}

def updateCheck(){
	setVersion()
	def lastKnownVersionStatus = state.versionStatus
	if (state?.versionStatus == null) { state.versionStatus = "<i>Unknown</i>" }
	def paramsUD = [uri: "https://lowrance.cc/ST/manifests/RayzurCodeHE.json"]
	if (updateCheckAllowed() || lastKnownVersionStatus == "<i>Unknown</i>" || lastKnownVersionStatus == null){
		state.Copyright = ""
		state.updateURI = ""
		state.UpdateInfo = ""
		state.author = ""
		state.versionBtn = ""
		def newVerRaw = ""
		def newVer = ""
		def currentVer = ""
		try {
			httpGet(paramsUD) { respUD ->
 			  //log.warn " Version Checking - Response Data: ${respUD.data}"   // Troubleshooting Debug Code 
				def copyrightRead = (respUD.data.copyright)
				//Manifest content is our own; keep any HTML entity intact for display and roll the copyright year forward
				state.Copyright = copyrightRead?.replaceFirst(/\b(19|20)\d{2}\b/, new Date().format("yyyy"))
				def updateUri = (respUD.data.versions.UpdateInfo.GithubFiles.(state.InternalName))
				state.updateURI = updateUri   
				newVerRaw = (respUD.data.versions.Application.(state.InternalName))
				if (newVerRaw == null) {
					//Not listed in the update manifest (e.g. DEV builds); nothing to compare against
					state.versionStatus = "Current"
					log.info "updateCheck(): ${state.InternalName} not found in update manifest; skipping version comparison"
					return
				}
				newVer = newVerRaw
				def newVerCompare = newVer.toString().replaceAll(/[^0-9.]/, '')
				currentVer = state.version.toString().replaceAll(/[^0-9.]/, '')
				state.UpdateInfo = (respUD.data.versions.UpdateInfo.Application.(state.InternalName))
				state.author = (respUD.data.author)
				log.debug "currentVer=${currentVer}, newVer=${newVer}"
				state.updateCheckFailures = 0
				if(newVer == "NLS"){
					state.versionStatus = "<b>** This app is no longer supported by ${state.author}  **</b>"  
					log.warn "** This app is no longer supported by ${state.author} **" 
				}
				else if(isNewerVersion(newVerCompare, currentVer)){
					state.versionStatus = "New Version Available (Version: ${newVerRaw})"
					log.warn "** There is a newer version of this app available  (Version: ${newVerRaw}) **"
					log.warn "** ${escapeHtml(state.UpdateInfo ?: "")} **"
					state.versionBtn = "UPDATE AVAILABLE"
					def updateMsg = "There is a new version of '${state.ExternalName}' available (Version: ${newVerRaw})"
					//pushOverNow(updateMsg)
				} 
				else{ 
					state.versionStatus = "Current"
					log.info "You are using the current version of this app"
				}
			}
		} catch (e) {
			state.updateCheckFailures = (state?.updateCheckFailures ?: 0) + 1
			log.error "Something went wrong: CHECK THE JSON FILE AND IT'S URI -  $e"
		}
		if(state.versionStatus != "Current"){
			state.versionBtn = "UPDATE AVAILABLE"
		}
		else{
			state.versionBtn = "No Update Available"
		}
	} else {
		return
	}
}

def isNewerVersion(newVer, currentVer){
	//Guard: only accept dotted-numeric version strings
	if (!(newVer.toString() ==~ /[0-9]+(\.[0-9]+)*/)) { return false }
	if (!(currentVer.toString() ==~ /[0-9]+(\.[0-9]+)*/)) { return false }
	def newParts = newVer.toString().tokenize('.')*.toInteger()
	def curParts = currentVer.toString().tokenize('.')*.toInteger()
	for (int i = 0; i < Math.max(newParts.size(), curParts.size()); i++) {
		int n = i < newParts.size() ? newParts[i] : 0
		int c = i < curParts.size() ? curParts[i] : 0
		if (n > c) { return true }
		if (n < c) { return false }
	}
	return false
}

def updateCheckAllowed(){
	// rayzurbock code, used with Cobra update code
	def proceed = false
	def updateCheckCurrentDate = new Date().getTime()
	def updateCheckIntervalInMil = (state.updateActiveUseIntervalMin * 60000)
	if ((state?.updateCheckFailures ?: 0) >= 3) { updateCheckIntervalInMil = updateCheckIntervalInMil * 4 }
	if (state?.updateNextCheckDate == null) { state.updateNextCheckDate = new Date().getTime() }
	def timeDiff = state?.updateNextCheckDate - updateCheckCurrentDate
	if (timeDiff <= 0 || timeDiff == null) { proceed = true }
	if (state?.updateNextCheckDate == null) { proceed = true }
	//if (!(proceed == true)) { log.debug "updateCheckAllowed() result: not allowed to proceed"}
	LOGDEBUG("updateCheckAllowed() timeDiff=${timeDiff}, proceed=${proceed}")
	if (proceed) {
		//log.debug("updateCheckAllowed(): result: proceed")
		state.updateNextCheckDate = new Date().getTime() + updateCheckIntervalInMil
		return true
	} else { 
		//log.debug("updateCheckAllowed(): result: do not proceed (${timeDiff})")
		return false
	}
	
}

def setFormatting(){
	state.formatSettingRootStart = "<B><span style='color: blue;'>"
	state.formatSettingRootEnd = "</span></B>"
	state.formatSettingOptionalStart = "<B><span style='color: #6897bb;'>"
	state.formatSettingOptionalEnd = "</font></B>"
    state.formatUlStart = "<ul>"
    state.formatUlEnd = "</ul>"
    state.formatLiStart = "<li>"
    state.formatLiEnd = "</li>"
    state.formatIStart = "<i>"
    state.formatIEnd = "</i>"
    state.formatStrongStart = "<strong>"
    state.formatStrongEnd = "</strong>"
    state.formatHr = "<hr>"
    state.formatBr = "<br>"
    state.formatCenterStart = "<center>"
    state.formatCenterEnd = "</center>"
}
    /*
    	Place in PARENT functions that need formatting:
    	def formatSettingRootStart = state.formatSettingRootStart
		def formatSettingRootEnd = state.formatSettingRootEnd
		def formatSettingOptionalStart = state.formatSettingOptionalStart
		def formatSettingOptionalEnd = state.formatSettingOptionalEnd
        def formatUlStart = state.formatUlStart
		def formatUlEnd = state.formatUlEnd
        def formatLiStart = state.formatLiStart
		def formatLiEnd = state.formatLiEnd
        def formatIStart = state.formatIStart
		def formatIEnd = state.formatIEnd
        def formatStrongStart = state.formatStrongStart
		def formatStrongEnd = state.formatStrongEnd
        def formatHr = state.formatHr
        def formatBr = state.formatBr
        def formatCenterStart = state.formatCenterStart
        def formatCenterEnd = state.formatCenterEnd
        
        Place in CHILD functions that need formatting:
        def formatSettingRootStart = parent.returnVar("formatSettingRootStart")
		def formatSettingRootEnd = parent.returnVar("formatSettingRootEnd")
		def formatSettingOptionalStart = parent.returnVar("formatSettingOptionalStart")
		def formatSettingOptionalEnd = parent.returnVar("formatSettingOptionalEnd")
        def formatUlStart = parent.returnVar("formatUlStart")
		def formatUlEnd = parent.returnVar("formatUlEnd")
        def formatLiStart = parent.returnVar("formatLiStart")
		def formatLiEnd = parent.returnVar("formatLiEnd")
        def formatIStart = parent.returnVar("formatIStart")
		def formatIEnd = parent.returnVar("formatIEnd")
        def formatStrongStart = parent.returnVar("formatStrongStart")
		def formatStrongEnd = parent.returnVar("formatStrongEnd")
        def formatHr = parent.returnVar("formatHr")
		def formatBr = parent.returnVar("formatBr")
        def formatCenterStart = parent.returnVar("formatCenterStart")
		def formatCenterEnd = parent.returnVar("formatCenterEnd")
    */

def setVersion(){
		//Cobra update code, modified by Rayzurbock
		state.version = "3.0.0"	 
		state.InternalName = "BigTalker3-Parent" 
		state.ExternalName = "BigTalker3"
		state.updateActiveUseIntervalMin = 30 //time in minutes to check for updates while using the App
}
