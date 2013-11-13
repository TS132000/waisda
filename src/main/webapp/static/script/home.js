$(function() {
	$('#reloadChannelsButton').click(function(e) {
		e.preventDefault();
		$.get('/channels', function(data) {
			$('#channels').html(data);
		});
	});
});
