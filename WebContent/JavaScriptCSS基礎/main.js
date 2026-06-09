$( function () {
    $('#typo').css('color','#ebc000');
});
$( function () {
    $('#typo').on('mouseover', function(){
        $('#typo').css('color', '#ebc000');
    });
});
$(function(){
	$('#typo').on('click', function(){
		$('.inner').animate({
			opacity: 0,
			fontSize: '0px'
		},
		1500
		);
	});
});