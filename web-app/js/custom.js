$(document).popover({
	selector: '[data-toggle="popover"]',
	html: true,
	placement: 'bottom',
	content: function() {
		return $(this).next('.popover').html();
	}

});

$(document).tooltip({
	selector: '[rel="tooltip"]'
});

$(document).ready(function() {

	//
	// $('body').on('click', '.link-wrapper .popover-trigger', function(e) {
	// 	e.preventDefault();
	// 	e.stopPropagation();
	// });

	// inicia el timeago
	$('time.timeago').timeago();

	// animo la progress-bar de boxes.likes
	$('.likes .progress-bar').progressbar({
		done: function() {
	    		var posTooltip = $('.progress-bar').width();
	    		console.log(posTooltip);
                $('#m-callback-done').css('left', posTooltip).css('opacity', '1');
            }
	});

	//tooltip visible sobre la progress bar
	$('.progress-bar').tooltip({trigger: 'manual', placement: 'top'}).tooltip('show');

	$("a.loadMore").on("click", function(e){loadMore(e, this)})
	// load more
	function loadMore(e, that) {

		e.preventDefault()
		var link = $(that)
		var url = link.attr('href')
		var parentId = link.attr('data-parent-id')
		var offset = link.attr('data-offset') || 10
		var loadingId = parentId+"-loading"
		var parent = $("#"+parentId)
		parent.append('<div class="loading" id="'+loadingId+'"><span class="sr-only">Cargando...</span></div>')
		$.ajax( {
			url:url,
			data:"offset="+offset,
			statusCode: {
				401: function() {
					location.reload();
				}
			}
		})
		.done(function(data, status, xhr) {
			parent.append(data).show('slow')
			var moreResults = xhr.getResponseHeader('moreResults')
			link.attr('data-offset', offset +10)
			if (moreResults){
				link.remove()
			}
		})
		.fail(function(data) {
			console.log(data)
		})
		.always(function(data) {
			$("#"+loadingId).remove()
			$("time.timeago").timeago();
		});
	}

	// apertura de karma
	function openKarma () {
		$('#karma').modal('show');
	}

	// para probar los avisos
	$('.alerts .btn').click(function() {
		notyError();
	});

	// para probar la apertura del karma
	// $('body').on('click', '.alerts .btn', function() {
	// 		openKarma();
 // 	});


	// al hacer clic en los badges vacía el contenido para que desaparezca
	$(function() {
		$('.badge').closest('a').click(function() {

			$(this).find('.badge').delay(2000).fadeOut("slow").queue(function() {
				$(this).empty();
			});
			$(this).next('ul').find('li.new').delay(2000).queue(function() {
				$(this).removeClass('new');
			});

			var url = $(this).attr("href")
			$.ajax(url);
		});
	});


	// links kakareo, impulsar
	$('.action.cluck, .action.drive').click( function(e) {
		e.preventDefault();
        e.stopPropagation();
        if (!$(this).hasClass('disabled')){
            var url = $(this).attr("href");
            var postId = $(this).parents("article").first().attr("data-cluck-postId");
            var cssClass = $(this).parent().hasClass("kakareo-number")?"kakareo-number":"like-number";
            $.ajax(url).done(function(data){
                console.log("article[data-cluck-postId='"+postId+"'] li."+cssClass+" .action");
                $("article[data-cluck-postId='"+postId+"'] li."+cssClass+" .action").addClass('disabled');
                $("article[data-cluck-postId='"+postId+"'] li."+cssClass+" .counter").each(function(idx, element){
                    var numKakareos = parseInt($(element).text()) +1;
                    $(element).text(numKakareos);
                });
            });
        }
	});


	// leer después
	$('body').on('click', '.read-later a', function(e) {
        e.preventDefault();
        var url = $(this).attr("href");
        var postId = $(this).parents("article").first().attr("data-cluck-postId");
        $.ajax(url).done(function(data, status, xhr){
            var isFavorite = xhr.getResponseHeader('isFavorite');
             var numFavorites = xhr.getResponseHeader('numList');
            $(".pending h1 .badge").text(numFavorites);
            if (isFavorite == "true"){
                $("article[data-cluck-postId='"+postId+"'] li.read-later a").addClass("disabled");
                $("article[data-cluck-postId='"+postId+"'] li.read-later a").removeClass("enabled");
                $("section.boxes.guay.pending ul.kakareo-list").prepend(data);
            }else{
                $("article[data-cluck-postId='"+postId+"'] li.read-later a").removeClass("disabled");
                $("article[data-cluck-postId='"+postId+"'] li.read-later a").addClass("enabled");
                $("section.boxes.guay.pending article[data-cluck-postId='"+postId+"']").parent().remove();
            }
        });

	});


	// Habilitar/deshabilitar link "Marcar como inapropiado"
	$('body').on("click", ".mark a", function() {
		if ( $(this).hasClass('disabled') ){
			$(this).removeClass('disabled');
		} else {
			$(this).addClass('disabled');
		}
	});


	// Habilitar/deshabilitar botón "Seguir" en Popover
	$('body').on("click", "#follow", function() {
		if ( $(this).hasClass('disabled') ){
			$(this).text('Seguir').removeClass('disabled');
		} else {
			$(this).html('Siguiendo <span class="fa fa-check-circle"></span>').addClass('disabled');
		}
	});


	// hacer clic en player falso del video (.front)
	$('.front').click( function() {
		$(this).next('.youtube').css('display', 'block');
		$(this).remove();
		return false;
	});


	// Buscador: cambia el placeholder según el filtro elegido
	$(function() {

		var $ui = $('#search-form');
		$ui.find('#filters li a').bind('focus click',function(){
			var filtro = $(this).html();
			$ui.find('#srch-term').attr('placeholder', filtro);
		});

	});


	// hacer un bloque clickable y que tome
	// que es su primer elemento la url del enlace a.hidden
	$(function() {

		$('.link-wrapper').click( function() {
			window.location = $(this).find('a.hidden').attr('href');
		});

	});


	// change text when select option in the edit post form
	$('#updateText').text($('#typePubli li.active').text());
	$('#selectType').change(function(){
        	$('#updateText').text($('#typePubli li').eq(this.selectedIndex).text());
    });


	// countdown
	$(function() {
		var totalChars      = parseInt($('#charInit span').text());
		var countTextBox    = $('.counted'); // Textarea input box
		var charsCountEl    = $('#charNum span'); // Remaining chars count will be displayed here

        if (countTextBox.length> 0){
		    charsCountEl.text(totalChars - countTextBox.val().length); //initial value of countchars element
        }
		countTextBox.keyup(function() { //user releases a key on the keyboard

			var thisChars = this.value.replace(/{.*}/g, '').length; //get chars count in textarea

			if (thisChars > totalChars) //if we have more chars than it should be
			{
				var CharsToDel = (thisChars-totalChars); // total extra chars to delete
				this.value = this.value.substring(0,this.value.length-CharsToDel); //remove excess chars from textarea
			} else {
				charsCountEl.text( totalChars - thisChars ); //count remaining chars
			}
		});
	});

	// textarea editor
	$(".texteditor").jqte({
		br: true,
		center: false,
		color: false,
		format: false,
		indent: false,
		left: false,
		ol: false,
		outdent: false,
		p: false,
		placeholder: "Escribe un texto que describa tu publicación",
		linktypes: ["URL", "Email"],
		remove: false,
		right: false,
		rule: false,
		source: false,
		sub: false,
		strike: false,
		sup: false,
		ul: false,
		unlink: false,
		fsize: false,
		title: false
	});


	// hacer visible la contraseña
	$('#show-pass').attr('checked', false);

	$('#show-pass').click(function(){

		if ($(this).hasClass('checked')) {
			$(this).removeClass('checked');
		} else {
			$(this).addClass('checked');
		}

	    name = $('#password').attr('name');
	    value = $('#password').val();

	    if($(this).hasClass('checked')) {
	    	html = '<input type="text" name="'+ name + '" value="' + value + '" id="password" class="form-control input-lg">';
	        $('#password').after(html).remove();
	    } else {
	    	html = '<input type="password" name="'+ name + '" value="' + value + '" id="password" class="form-control input-lg">';
	    	$('#password').after(html).remove();
	    }
	});

    var texts= {
        0: i18n.customRegister.step4.form.submit.description0,
        1: i18n.customRegister.step4.form.submit.description1,
        2: i18n.customRegister.step4.form.submit.description2,
        ok:i18n.customRegister.step4.form.submit.descriptionOk
    }

    function changeDescriptionNumSelect(){
        var numChecked = $("#sign4 input[type=checkbox]:checked").length
        if (numChecked < 3){
            $("#descNumSelect").html(texts[numChecked])
            $("#sign4 input[type=submit]").addClass('disabled')
        }else{
            $("#descNumSelect").html(texts['ok'])
            $("#sign4 input[type=submit]").removeClass('disabled')
        }
    }
	// seleccionar todos los checkbox
	$(function () {
        changeDescriptionNumSelect()
	    var checkAll = $('#selectAll');
	    var checkboxes = $('input.check');

	    $('input.check').each(function(){
		    var self = $(this),
		    label = self.next(),
		    label_text = label.html();
		    label.remove();
		    self.iCheck({
		      checkboxClass: 'icheckbox_line-orange',
		      radioClass: 'iradio_line-orange',
		      inheritID: true,
		      aria: true,
		      insert:  label_text
		    });
		});

	    $('#selectAll').change(function() {
		    if($(this).is(':checked')) {
		        checkboxes.iCheck('check');
		        $('#others').attr('checked', true);
		    } else {
		        checkboxes.iCheck('uncheck');
		        $('#others').attr('checked', false);
		    }
		});


	    checkAll.on('ifChecked ifUnchecked', function(event) {
	        if (event.type == 'ifChecked') {
	            checkboxes.iCheck('check');
	        } else {
	            checkboxes.iCheck('uncheck');
	        }
	    });

	    checkboxes.on('ifChanged', function(event){
	        if(checkboxes.filter(':checked').length == checkboxes.length) {
	            checkAll.prop('checked', 'checked');
	        } else {
	            checkAll.removeProp('checked');
	        }
	        checkAll.iCheck('update');
            changeDescriptionNumSelect();
	    });
	});

	// añade la flechita al span de los mensajes de error de los formularios
	$('span.error').prepend('<span class="tooltip-arrow"></span>');

	// le da la clase error al falso textarea
	$(function () {
		if ( $('#textPost').hasClass('error') ) {
				$('#textPost').closest('.jqte').addClass('error');
			}
	});

	// inicializa el scroll dentro del popover
	$('.popover-trigger.more-users').on('shown.bs.popover', function () {

		$(this).next('.popover').find($('.scroll')).slimScroll({
			size: '10px',
			height: '145px',
			distance: '0',
			railVisible: true,
			alwaysVisible: true,
			disableFadeOut: true
		});

	})

});


// funciones que llaman a las diferentes notificacones (salen en la parte superior de la pantalla)
	var display = {
    error:function(text){this._notyGeneric(text, "error")},
    success:function(text){this._notyGeneric(text, "success")},
    info:function(text){this._notyGeneric(text, "information")},
    warn:function(text){this._notyGeneric(text, "warning")},

    _notyGeneric:function(text, type) {
        var nW = noty({
            layout: 'top',
            dismissQueue: true,
            animation: {
                open: {height: 'toggle'},
                close: {height: 'toggle'},
                easing: 'swing',
                speed: 500 // opening & closing animation speed
            },
            template: '<div class="noty_message" role="alert"><span class="noty_text"></span><div class="noty_close"></div></div>',
            type: type,
            text: text
        });
    }
}


// el hover sobre el kakareo que afecte al triángulo superior
$(document).ajaxStop(function () {

	$('.kakareo > .link-wrapper').on({
	    mouseenter: function () {
	        $(this).prev('.from').find('.inside').css('border-bottom', '8px solid #efefef');
	    },
	    mouseleave: function () {
	        $(this).prev('.from').find('.inside').css('border-bottom', '8px solid #fafafa');
	    }
	});

	$('.important .kakareo > .link-wrapper').on({
	    mouseenter: function () {
	        $(this).prev('.from').find('.inside').css('border-bottom', '8px solid #feedce');
	    },
	    mouseleave: function () {
	        $(this).prev('.from').find('.inside').css('border-bottom', '8px solid #fff8ed');
	    }
	});

});