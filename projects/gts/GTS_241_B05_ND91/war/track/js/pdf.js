 
	function PrintContent(title) 
		{			 
			var DocumentContainer = document.getElementById('dataTable');
			var WindowObject = window.open('_blank', 'PrintWindow', 'toolbars=no,scrollbars=yes,status=no ');
			WindowObject.document.writeln('<!DOCTYPE html>');
			WindowObject.document.writeln('<html><head><title></title>');			
			WindowObject.document.writeln('</head><body>')
			WindowObject.document.writeln('<h3>'+title+'</h3>');
			WindowObject.document.writeln(DocumentContainer.innerHTML);
			WindowObject.document.writeln('<style> *{margin:0;padding:0;text-align:center;} table{ border-spacing: 0px;border-collapse: collapse;} td,th{padding:2px;border: 1px solid #DDD;} h3{text-align:center;padding:10px 5px;} </style>');
			WindowObject.document.writeln('</body></html>');
			WindowObject.document.close();
			WindowObject.focus();
			WindowObject.print();
			WindowObject.close();
		}	
 