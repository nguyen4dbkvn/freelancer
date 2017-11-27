(function( $ ) {
$.widget( "custom.combobox", {
_create: function() {
	this.wrapper = $( "<span>" )
	.addClass( "custom-combobox" )
	.insertAfter( this.element );
	this.element.hide();
	this._createAutocomplete();
	this._createShowAllButton();
},
_createAutocomplete: function() {
	var selected = this.element.children( ":selected" ),
	value = selected.val() ? selected.text() : "";
	var id = this.element.attr('id');
	this.input = $( "<input>" )
	.appendTo( this.wrapper )
	.val( value )
	.attr( "id", id+"_text" )
	.attr( "name", id+"_text" )
	.attr( "title", "" )
	.addClass( "textInputAutoComplete" )
	.autocomplete({
	delay: 0,
	minLength: 0,
	source: $.proxy( this, "_source" ),
	
	
        select: function( event, ui ) {
        	//var svalue = $("#carcolor").val();
//			document.getElementById('hidAccount').value = $("#Account").val();
					
			if(document.getElementById('hidAccount').value != $("#AccID").val()){
				var svalue = $("#AccID").val();
				if($("#AccID").val() != null && $("#AccID").val() != ""){
					document.getElementById('hidAccount').value = $("#AccID").val();
				}else{
					
				}
				LoadDeviceByAccount(svalue);
			}
			else
			{
				document.getElementById('hidDevice').value = $("#device").val();
			}
			document.getElementById('hidDevice').value = $("#device").val();
			
			
        }
//        change: function(event, ui) {
//        	var svalue = $("#carmaker").val();
//        	LoadModelByMaker(svalue);
//        }
	})  
	.tooltip({
	tooltipClass: "ui-state-highlight"
	});
	this._on( this.input, {
	autocompleteselect: function( event, ui ) {
	ui.item.option.selected = true;
	this._trigger( "select", event, {
	item: ui.item.option
	});
	}/*,
	autocompletechange: "_removeIfInvalid"*/
	});
},
_createShowAllButton: function() {
	var input = this.input,
	wasOpen = false;
	$( "<a>" )
	.attr( "tabIndex", -1 )
	.attr( "title", "" )
	.tooltip()
	.appendTo( this.wrapper )
	.button({
	icons: {
	primary: "ui-icon-triangle-1-s"
	},
	text: false
	})
	.removeClass( "ui-corner-all" )
	.addClass( "custom-combobox-toggle ui-corner-right" )
	.mousedown(function() {
	wasOpen = input.autocomplete( "widget" ).is( ":visible" );
	})
	.click(function() {
	input.focus();
	// Close if already visible
	if ( wasOpen ) {
	return;
	}
	// Pass empty string as value to search for, displaying all results
	input.autocomplete( "search", "" );
	});
},
_source: function( request, response ) {
	var matcher = new RegExp( $.ui.autocomplete.escapeRegex(request.term), "i" );
	response( this.element.children( "option" ).map(function() {
	var text = $( this ).text();
	if ( this.value && ( !request.term || matcher.test(text) ) )
	return {
	label: text,
	value: text,
	option: this
	};
	}) );
},
_removeIfInvalid: function( event, ui ) {
	// Selected an item, nothing to do
	if ( ui.item ) {
	return;
	}
	// Search for a match (case-insensitive)
	var value = this.input.val(),
	valueLowerCase = value.toLowerCase(),
	valid = false;
	this.element.children( "option" ).each(function() {
	if ( $( this ).text().toLowerCase() === valueLowerCase ) {
	this.selected = valid = true;
	return false;
	}
	});
	// Found a match, nothing to do
	if ( valid ) {
	return;
	}
	// Remove invalid value
	this.input
	.val( "" )
	.attr( "title", "" )
	.tooltip( "open" );
	this.element.val( "" );
	this._delay(function() {
	this.input.tooltip( "close" ).attr( "title", "" );
	}, 2500 );
	this.input.data( "ui-autocomplete" ).term = "";
},
_destroy: function() {
	this.wrapper.remove();
	this.element.show();
	}
});
})( jQuery );
$(function() {
$( "#AccID" ).combobox();
$( "#device" ).combobox();
});