/**
 * 
 */

$(document).ready(function() {
	if(localStorage.getItem("RemoveItemSuccess")) {
		toastr.info('Il prodotto è stato rimosso');
	    localStorage.clear();
	}
	if(localStorage.getItem("RemoveItemsSuccess")) {
		toastr.info('I prodotti selezionati sono stati rimossi.');
	    localStorage.clear();
	}
	
	$('.delete-product').on('click', function (){
		/*<![CDATA[*/
	    var path = /*[[@{/}]]*/'remove';
	    /*]]>*/
		
		var id=$(this).attr('id');

		bootbox.confirm({
			message: "Sicuro di voler rimuovere questo prodotto? L\'operazione è irriversibile.",
			buttons: {
				cancel: {
					label:'<i class="fa fa-times"></i> Cancel'
				},
				confirm: {
					label:'<i class="fa fa-check"></i> Confirm'
				}
			},
			callback: function(confirmed) {
				if(confirmed) {
					$.ajax({
						type: 'POST',
						url: path,
						data: {'id':id},
						contentType: "application/json",
						dataType: "json",
						success: function(res) {
							var obj = JSON.parse(res);
							
							if(!obj) {
								toastr.error('Attenzione! Non puoi rimuovere questo prodotto.');
							}
							else  {
								localStorage.setItem("RemoveItemSuccess", true);
								location.reload();
							}
							console.log(res); 
							},
						error: function(res){
							console.log(res); 
							location.reload();
							}
					});
				}
			}
		});
	});
	
	
	
//	$('.checkboxBook').click(function () {
//        var id = $(this).attr('id');
//        if(this.checked){
//            bookIdList.push(id);
//        }
//        else {
//            bookIdList.splice(bookIdList.indexOf(id), 1);
//        }
//    })
	
	$('#deleteSelected').click(function() {
		var idList= $('.checkboxProduct');
		var bookIdList=[];
		for (var i = 0; i < idList.length; i++) {
			if(idList[i].checked==true) {
				bookIdList.push(idList[i]['id'])
			}
		}
		
		console.log(bookIdList);
		
		/*<![CDATA[*/
	    var path = /*[[@{/}]]*/'removeList';
	    /*]]>*/
	    
	    bootbox.confirm({
			message: "Sicuro di voler rimuovere i prodotti selezionati? L\'operazione è irriversibile.",
			buttons: {
				cancel: {
					label:'<i class="fa fa-times"></i> Cancel'
				},
				confirm: {
					label:'<i class="fa fa-check"></i> Confirm'
				}
			},
			callback: function(confirmed) {
				if(confirmed) {
					$.ajax({
						type: 'POST',
						url: path,
						data: JSON.stringify(bookIdList),
						contentType: "application/json",
						success: function(res) {
							if(res){
								localStorage.setItem("RemoveItemsSuccess", true);
								location.reload()
							}
							else {
								toastr.error('Attenzione! Non puoi rimuovere i prodotti selezionati.');
							}	
							console.log(res); 
							},
						error: function(res){
							console.log(res); 
							location.reload();
							}
					});
				}
			}
		});
	});
	
	$("#selectAllProduct").click(function() {
		if($(this).prop("checked")==true) {
			$(".checkboxProduct").prop("checked",true);
		} else if ($(this).prop("checked")==false) {
			$(".checkboxProduct").prop("checked",false);
		}
	})
});

$(document).ready(function(){
    $("#btn1").click(function(){
        $("p").append(" <b>Appended text</b>.");
    });
});