(function($) {
  $(document).ready(function() {
    $('input').placeholder();
    
    /* TODO validation + error handling */
    $('#lssn').submit(function(e) {
      e.preventDefault();
      $.post(this.action, $(this).serialize(), function(e) {
        var id = 'url-' + e.shortUrl;
        if($('#' + id).length < 1) {
          var url = 'http://lssn.me/' + e.shortUrl;
          var link = $('<li>').append(
            $('<a>').attr({ id: id, href: url }).text(url)
          ).hide();
          $('#shortened').prepend(link.fadeIn());
        }
      });
    })
  });
})(jQuery);
