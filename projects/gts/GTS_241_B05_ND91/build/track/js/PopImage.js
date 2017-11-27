var listimage;
var idx = 0;
function NextImage()
{
	var arrimage = listimage.split(";");
	if(idx<arrimage.length -1)
	idx++;
	if(arrimage[idx]!="")
	document.getElementById ("imgshow").src  = arrimage[idx];
}
function BackImage()
{
	var arrimage = listimage.split(";");
	if(idx>0)
	idx--;
	if(arrimage[idx]!="")
	document.getElementById ("imgshow").src  = arrimage[idx];
}
function idxre(str)
{
	var arrimage = listimage.split(";");
	var ire = 0;
	for(var i = 0; i<arrimage.length; i++)
		{
			if(arrimage[i]== str)
				ire = i;
		}
	return ire;
}
$(document).ready(function(){
        						   		   
	        
	        $('a.poplight[href^=#]').click(function() {
		        var popID = $(this).attr('rel'); 
		        var popURL = $(this).attr('href'); 
        	   
		        var query= popURL.split('?');
		        fanh = query[1];
		        idx = idxre(fanh);
		        document.getElementById ("imgshow").src =fanh;
		        
		        var dim= query[1].split('&');
		        var popWidth = dim[0].split('=')[1]; 

		       
		        $('#' + popID).fadeIn().css({ 'width': Number( popWidth ) }).prepend('<a href="#" class="close"><img src="./images/close_en.gif" class="btn_close" title="Close Window" alt="Close" /></a>');
        		
		        
		        var popMargTop = ($('#' + popID).height() + 80) / 2;
		        var popMargLeft = ($('#' + popID).width() + 80) / 2;
        		
		        
		        $('#' + popID).css({ 
			        'margin-top' : -popMargTop,
			        'margin-left' : -popMargLeft
		        });
        		
		        
		        $('body').append('<div id="fade"></div>'); 
		        $('#fade').css({'filter' : 'alpha(opacity=80)'}).fadeIn(); 
        		
		        return false;
	        });
	        
	        $('a.close, #fade').live('click', function() { 
	  	        $('#fade , .popup_block').fadeOut(function() {
			        $('#fade, a.close').remove();  
	        }); 
        		
		        return false;
	        });

        	
        });
