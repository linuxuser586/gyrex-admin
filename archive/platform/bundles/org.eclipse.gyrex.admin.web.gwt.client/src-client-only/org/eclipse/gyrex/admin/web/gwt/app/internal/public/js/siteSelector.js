YAHOO.namespace('nova.buttons');
YAHOO.nova.buttons.initButtons = function () {
	var osButton = new YAHOO.widget.Button("otherSitesButton" );

	var showAccordion = new YAHOO.util.Anim('sitesHolder', { height: { from:0, to:10, unit: 'em'} }, 1, YAHOO.util.Easing.easeOutStrong);
	var hideAccordion = new YAHOO.util.Anim('sitesHolder', { height: { from:10, to:0, unit: 'em'} }, 1, YAHOO.util.Easing.easeOutStrong);
	var el = YAHOO.util.Dom.get("sitesHolder");
	var arrow = YAHOO.util.Dom.get("arrow");
	
	var saStart = function (type, args) { 
		el.className = "visible"; 
		arrow.className = "visible";
		  YAHOO.util.Cookie.set("siteShow", "visible", {
		        path: "/"         //all pages
		    });

	}	
	
	var haEnd = function (type, args) {
		el.className = "invisible";
		  YAHOO.util.Cookie.set("siteShow", "invisible", {
		        path: "/"          //all pages
		    });		
	}
	
	var haStart = function(type, args) {
		arrow.className = "invisible";
	}
	
	showAccordion.onStart.subscribe(saStart);
	hideAccordion.onStart.subscribe(haStart);	
	hideAccordion.onComplete.subscribe(haEnd);

	
	var fnToggleSites = function() {
		
		if (el.className  == "invisible")
		{
			showAccordion.animate();
		}
		else {
			hideAccordion.animate();
		}
		
	}
	
	osButton.addListener("click", fnToggleSites);
	
}
YAHOO.util.Event.onContentReady("otherSitesButton", YAHOO.nova.buttons.initButtons);




