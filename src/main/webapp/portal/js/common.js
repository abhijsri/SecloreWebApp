// For below IE 9 Support
if(!String.prototype.trim)
{
	// console.log("define custom trim function");
	String.prototype.trim= function()
	{
		return this.replace(/^\s+|\s+$/gm,'');	
	};
}
//For below IE 9 Support
if( !Array.prototype.indexOf )
{
	// console.log("define custom indexOf function");
	Array.prototype.indexOf = function(elt)
	  {
	    var len = this.length >>> 0;

	    var from = Number(arguments[1]) || 0;
	    from = (from < 0)
	         ? Math.ceil(from)
	         : Math.floor(from);
	    if (from < 0)
	      from += len;

	    for (; from < len; from++)
	    {
	      if (from in this &&
	          this[from] === elt)
	        return from;
	    }
	    return -1;
	  };	  
}

$(document).ready(function(){
	
	// Error or Info Message will be disappeared after specified time.
	/*
	$("#messageDiv").animate({opacity: 0}, 20000, function(){
		$("#messageDiv").empty();
	});
	*/
	
	// Check whether the browser support place holder or not 
    $.support.placeholder = ('placeholder' in document.createElement('input'));
    if (!$.support.placeholder) {
        $("[placeholder]").focus(function () {
            if( $(this).val() == $(this).attr("placeholder") )
            { 
            	$(this).val("");
            	$(this).css('color','#555');
            }
        }).blur(function () {
            if ( $(this).val() == "" ){
            	$(this).val( $(this).attr("placeholder") );
            	$(this).css('color','#999');
            }
        }).blur();

        $("[placeholder]").parents("form").submit(function () {
            $(this).find('[placeholder]').each(function() {
                if ( $(this).val() == $(this).attr("placeholder") )
                {
                    $(this).val("");
                }
            });
        });
    }
	
});