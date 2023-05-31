<script>
	function modalPopup(GetName) {
		var $layer = $("#" + GetName);
		$layer.addClass("on");
		$("body").css("overflow","hidden");
	}
 
	function closeModalPopup(GetName) {
		$("#" + GetName).removeClass("on");
		$("body").css("overflow","auto");
		$('#idConfirm').hide();
	}
	
	$(document).mouseup(function(e) {
		if ($(".modal-container").has(e.target).length === 0) {
			$(".modal").removeClass("on");
			$("body").css("overflow","auto");
		}
	});
	
    $.fn.setEgovModalClose = function close() {
    	$(this).css("display","none");
    };
    
	$.fn.egovModal = function(oModal) {
		//Get the modal
		var modal = document.getElementById(oModal);

		// When the user clicks the button, open the modal
		$(this).click(function(){
			$(modal).css("display","block");
		});
		
		$(this).find("#btnModalClose").click(function(){
    		$(this).parent().parent().parent().parent().css("display","none");
		});
    };

</script>

</body>
</html>
