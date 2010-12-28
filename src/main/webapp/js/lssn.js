(function($) {
  $(document).ready(function() {
    $('#lssn').submit(function(e) {
      e.preventDefault();
      $.post(this.action, $(this).serialize(), function(e) {
        var url = location.protocol + '//' + location.host + '/' + e.shortUrl;
        var link = $('<li>').append(
          $('<a>').attr({ href: url }).text(url)
        ).hide();
        $('#shortened').prepend(link.fadeIn());
      });
    })
  });
})(jQuery);
