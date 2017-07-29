/**
 * 
 */

$(document).ready(function() {
	if(localStorage.getItem("Success")) {
		toastr.info('L\'elemenoto è stato rimosso');
	    localStorage.clear();
	}
	
	$('.delete-cigarette').on('click', function (){
		/*<![CDATA[*/
	    var path = /*[[@{/}]]*/'remove';
	    /*]]>*/
		
		var id=$(this).attr('id');

		bootbox.confirm({
			message: "Sicuro di voler cancellare questo prodotto? Non puoi tornare indietro.",
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
								toastr.error('Attenzione! Non puoi rimuovere questo elemento');
							}
							else  {
								localStorage.setItem("Success", true);
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
		var idList= $('.checkboxCigarette');
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
			message: "Sicuro di voler cancellare questo/i prodotto/i? Non puoi tornare indietro.",
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
							console.log(res); 
							location.reload()
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
	
	$("#selectAllCigarette").click(function() {
		if($(this).prop("checked")==true) {
			$(".checkboxCigarette").prop("checked",true);
		} else if ($(this).prop("checked")==false) {
			$(".checkboxCigarette").prop("checked",false);
		}
	})
});