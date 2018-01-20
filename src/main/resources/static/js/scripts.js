/**
 * 
 */

function checkBillingAddress() {
	if ($("#theSameAsShippingAddress").is(":checked")) {
		$(".billingAddress").prop("disabled", true);
	} else {
		$(".billingAddress").prop("disabled", false);
	}
}

function checkPasswordMatch() {
	var password = $("#txtNewPassword").val();
	var confirmPassword = $("#txtConfirmPassword").val();
	var regexPassword = /^(?=.*[a-z])(?=.*[A-Z])(?=.*\d)[A-Za-z\d]{8,}$/;
	var pattPass = new RegExp(regexPassword);

	if (password == "" && confirmPassword == "") {
		$("#checkPasswordMatch").fadeOut();
		$("#checkPasswordMatch").html("");
		$("#updateUserInfoButton").prop('disabled', false);
	} else {
		if (password != confirmPassword) {
			$("#checkPasswordMatch").fadeIn();
			$("#checkPasswordMatch").html("Le password non coincidono!");
			$("#updateUserInfoButton").prop('disabled', true);
		} else if (!pattPass.test(password)) {
			var passLowerCase = /^(?=.*[a-z])/;
			var passUpperCase = /^(?=.*[A-Z])/;
			var passNum = /(?=.*\d)/;

			if (password.length < 8) {
				$("#checkPasswordMatch").html(
						"La password deve avere almeno 8 caratteri");
				$("#updateUserInfoButton").prop('disabled', true);
			} else if (!(new RegExp(passLowerCase)).test(password)) {
				$("#checkPasswordMatch").html(
						"La password deve avere almeno una lettera minuscola");
				$("#updateUserInfoButton").prop('disabled', true);
			} else if (!(new RegExp(passUpperCase)).test(password)) {
				$("#checkPasswordMatch").html(
						"La password deve avere almeno una lettera maiuscola");
				$("#updateUserInfoButton").prop('disabled', true);
			} else if (!(new RegExp(passNum)).test(password)) {
				$("#checkPasswordMatch").html(
						"La password deve avere almeno un numero");
				$("#updateUserInfoButton").prop('disabled', true);
			}
		} else {
			$("#checkPasswordMatch").fadeOut();
			$("#checkPasswordMatch").html("Le password coincidono");
			$("#updateUserInfoButton").prop('disabled', false);
		}
	}
}

function checkEmailFormat() {
	var regexEmail = /^([a-zA-Z0-9_.+-])+\@(([a-zA-Z0-9-])+\.)+([a-zA-Z0-9]{2,4})+$/;
	var pattEmail = new RegExp(regexEmail);
	var emailLogin = $("#email-login").val();

	if (!pattEmail.test(emailLogin)) {
		$("#checkEmailFormat").fadeIn();
		$("#checkEmailFormat").html(
				"Il formato dell'indirizzo email non è corretto");
		$("#registerButton").prop('disabled', true);
	} else {
		$("#checkEmailFormat").fadeOut();
		$("#checkEmailFormat").html("");
		$("#registerButton").prop('disabled', false);
	}
}

$(document).ready(function() {
	$("#theSameAsShippingAddress").on('click', checkBillingAddress);
	$("#txtConfirmPassword").keyup(checkPasswordMatch);
	$("#txtNewPassword").keyup(checkPasswordMatch);
	$("#email-login").keyup(checkEmailFormat); // validazione email in
	// customer-register.html
});

/**
 * Qui di seguito definisco ajax-jquery per la ricerca asincrona delle categorie
 */

$(".category-ajax").click(function(e) {
	// evito che venga eseguito l'href dell'ancora
	e.preventDefault();
	var id = $(this).attr("id");

	// ripulisco i prodotti caricati precedentemente
	$("#shop-category-call").empty();
	$("#div-elementi-categoria").empty();

	// rendo attivo il bottone della categoria selezionata, e non attivo
	// tutti gli altri
	$(".category-ajax").each(function(i, obj) {
		if(id==$(this).attr("id")){
			$("#"+id+"-list-item").addClass("active btn-default");
		} else {
			$("#"+$(this).attr("id")+"-list-item").removeClass("active");
		}
	});

	// chiamata ajax-jquery
	$("#div-elementi-categoria").load("/searchByCategoryAjax?category="+id);
	
});