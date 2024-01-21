(function ($) {
    "use strict";
    try {
        // Spinner
        var spinner = function () {
            setTimeout(function () {
                if ($("#spinner").length > 0) {
                    $("#spinner").removeClass("show");
                }
            }, 500);
        };
        spinner();


        // Initiate the wowjs
        new WOW().init();


        // Sticky Navbar
        $(window).scroll(function () {
            if ($(this).scrollTop() > 300) {
                $('.sticky-top').addClass('shadow-sm').css('top', '0px');
            } else {
                $('.sticky-top').removeClass('shadow-sm').css('top', '-150px');
            }
        });


        // Back to top button
        $(window).scroll(function () {
            if ($(this).scrollTop() > 300) {
                $('.back-to-top').fadeIn('slow');
            } else {
                $('.back-to-top').fadeOut('slow');
            }
        });
        $('.back-to-top').click(function () {
            $('html, body').animate({ scrollTop: 0 }, 1500, 'easeInOutExpo');
            return false;
        });


        // Modal Video
        var $videoSrc;
        $('.btn-play').click(function () {
            $videoSrc = $(this).data("src");
        });
        console.log($videoSrc);
        $('#videoModal').on('shown.bs.modal', function (e) {
            $("#video").attr('src', $videoSrc + "?autoplay=1&amp;modestbranding=1&amp;showinfo=0");
        })
        $('#videoModal').on('hide.bs.modal', function (e) {
            $("#video").attr('src', $videoSrc);
        })


        // Product carousel
        $(".product-carousel").owlCarousel({
            autoplay: true,
            smartSpeed: 1000,
            margin: 25,
            loop: true,
            center: true,
            dots: false,
            nav: true,
            navText: [
                '<i class="bi bi-chevron-left"></i>',
                '<i class="bi bi-chevron-right"></i>'
            ],
            responsive: {
                0: {
                    items: 1
                },
                576: {
                    items: 1
                },
                768: {
                    items: 2
                },
                992: {
                    items: 3
                }
            }
        });


        // Testimonial carousel
        $(".testimonial-carousel").owlCarousel({
            autoplay: true,
            smartSpeed: 1000,
            items: 1,
            loop: true,
            dots: true,
            nav: false,
        });
    } catch (error) {
        window.location.href = "/error-page";
    }
})(jQuery);



// Select first child of size radio buttons
document.addEventListener("DOMContentLoaded", function () {
    var sizeRadioButtons = document.querySelectorAll('input[type=radio][name=selectedSize]');

    if (sizeRadioButtons.length > 0) {
        sizeRadioButtons[0].checked = true;
    }
});

// Size choosing event
var sizeRadioButtons = document.querySelectorAll('input[type=radio][name=selectedSize]');
sizeRadioButtons.forEach((radio) => {
    radio.addEventListener('change', function () {
        var productId = this.dataset.productId;
        var sizeId = this.value;
        getProductPrice(productId, sizeId);
    });
});

// In case of a product without a discount
function getProductPrice(productId, sizeId) {
    // Fetch product price based on productId and sizeId
    fetch(`/getProductPriceBySize?productId=${productId}&sizeId=${sizeId}`)
        .then(response => response.json())
        .then(data => {
            var priceElement = document.getElementById(`product-price${productId}`);
            var discountedPriceElement = document.getElementById(`product-discountedPrice${productId}`);

            if (data >= 0) {
                // Get discountPercent from service
                getDiscountPercent(productId, data, priceElement, discountedPriceElement);

            }
        })
        .catch(error => {
            console.error('Error:', error);
        });
}

// Price format function
function formatPrice(price) {
    let formattedPrice = new Intl.NumberFormat('vi-VN', { style: 'currency', currency: 'VND' }).format(price);
    formattedPrice = formattedPrice.replace(/\./g, ',');
    return formattedPrice;
}

function getDiscountPercent(categoryID, sizePrice, priceElement, discountedPriceElement) {
    // Fetch discountPercent from service
    fetch(`/getDiscountPercentByCategoryId?categoryID=${categoryID}`)
        .then(response => response.json())
        .then(data => {
            var discountPercent = parseFloat(data);

            if (!isNaN(discountPercent) && discountPercent > 0) {
                // Calculate discounted price
                var discountedPrice = calculateDiscountedPrice(sizePrice, discountPercent);

                // Display original price in del
                //                priceElement.textContent = formatPrice(sizePrice);

                // Display the discounted price in h3
                if (discountedPriceElement) {
                    discountedPriceElement.textContent = formatPrice(sizePrice);
                }
            } else {
                // If no discount, display the original price in h3
                priceElement.textContent = formatPrice(sizePrice);

                // Clear the discounted price if no discount
                if (discountedPriceElement) {
                    discountedPriceElement.textContent = '';
                }
            }
        })
        .catch(error => {
            console.error('Error:', error);
        });
}

function calculateDiscountedPrice(originalPrice, discountPercent) {
    console.log(originalPrice);
    return originalPrice - (originalPrice * discountPercent); // Calculate the discounted price based on the discount percentage
}

// Enable send button
document.addEventListener("DOMContentLoaded", function () {
    var starRadios = document.querySelectorAll('input.star-check');
    var commentTextArea = document.getElementById('comment');
    var submitButton = document.getElementById('submitButton');

    var isStarSelected = false;
    var isCommentFilled = false;

    starRadios.forEach(function (radio) {
        radio.addEventListener('change', function () {
            isStarSelected = document.querySelector('input.star-check:checked') !== null;
            enableSubmitButton();
        });
    });

    commentTextArea.addEventListener('input', function () {
        isCommentFilled = commentTextArea.value.trim() !== '';
        enableSubmitButton();
    });

    function enableSubmitButton() {
        if (isStarSelected && isCommentFilled) {
            submitButton.disabled = false;
        } else {
            submitButton.disabled = true;
        }
    }
});

// Star filter listener
document.addEventListener("DOMContentLoaded", function () {
    var starRatingSelect = document.getElementById("starRating");
    var filterForm = document.getElementById("ratingFilter");

    var urlParams = new URLSearchParams(window.location.search);
    var selectedValue = urlParams.get("starRating");

    if (selectedValue) {
        starRatingSelect.value = selectedValue;
    }

    starRatingSelect.addEventListener("change", function () {
        selectedValue = starRatingSelect.value;

        var currentURL = new URL(window.location.href);
        currentURL.searchParams.set("starRating", selectedValue);

        filterForm.action = currentURL.toString();

        filterForm.submit();
    });
});

document.addEventListener("DOMContentLoaded", function () {
    var notificationIcon = document.querySelector(".notification-icon");
    var notificationDropdown = document.querySelector(".notification-dropdown");

    notificationIcon.addEventListener("click", function () {
        // Toggle the display of the dropdown
        if (notificationDropdown.style.display === "block") {
            notificationDropdown.style.display = "none";
        } else {
            notificationDropdown.style.display = "block";
        }
    });
});
