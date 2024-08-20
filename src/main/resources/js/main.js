 $(document).ready(function () {
            $("#fetch-products").click(function (event) {

                //stop submit the form, we will post it manually.
                event.preventDefault();
                fire_ajax_submit();

            });

        });

        function fire_ajax_submit() {

    var search = {}
    search["username"] = $("#username").val();

    $("#btn-search").prop("disabled", true);

    $.ajax({
        type: "GET",
        contentType: "application/json",
        url: "api/product/all",
        cache: false,
        timeout: 600000,
        success: function (data) {
            const products = JSON.parse(xhttp.responseText);
              var innerHTML = '<table> <tr><th>Id</th><th>Name</th><th>Price</th><th>Available quantity</th></tr>'
              for (let i = 0; i < products.length; i++) {
                 var product = products[i];
                 innerHTML = innerHTML + '<tr><td>'+ product.id+'</td><td>'+product.name+'</td><td>'+product.price+
                 '</td><td>'+product.quantity+'</td></tr>';
               }
              innerHTML = innerHTML + '</table>'
              document.getElementById("product-list").innerHTML = innerHTML;

        },
        error: function (e) {

        }
    });
    }