(function ($) {
  "use strict";

  // Spinner
  var spinner = function () {
    setTimeout(function () {
      if ($("#spinner").length > 0) {
        $("#spinner").removeClass("show");
      }
    }, 500); 
  };
  spinner();

    new WOW().init();

    // Sticky Navbar
    $(window).scroll(function () {
      if ($(this).scrollTop() > 300) {
        $(".sticky-top").addClass("shadow-sm").css("top", "0px");
      } else {
        $(".sticky-top").removeClass("shadow-sm").css("top", "-150px");
      }
    });

  // Back to top button
  $(window).scroll(function () {
    if ($(this).scrollTop() > 300) {
      $(".back-to-top").fadeIn("slow");
    } else {
      $(".back-to-top").fadeOut("slow");
    }
  });
  // Back to top button
  $(window).scroll(function () {
    if ($(this).scrollTop() > 300) {
      $(".back-to-top").fadeIn("slow");
    } else {
      $(".back-to-top").fadeOut("slow");
    }
  });

  // Show .cart-0 if cartCount is less than or equal to 0, otherwise show .cart-1
  // var cartCount = parseInt($("#cartCount").text());

  // if (cartCount <= 0) {
  //   $(".cart-0").show();
  //   $(".cart-1").hide();
  // } else {
  //   $(".cart-0").hide();
  //   $(".cart-1").show();
  // }

    $("#step-1").addClass("active-stext");
    // Show infor-cart and hide cart-0, cart-1 on Buy button click
    $("#buyButton").click(function () {
      $(".infor-cart").show();
      $(".cart-0, .cart-1, .form-buy").hide();

      // Add active class to number-2 and apply animation
      $("#number-2").addClass("active");
      $("#line-1").addClass("active-line");
      $("#step-2").addClass("active-stext");

      // Change color of number-2 to match .step-button.active
      $("#number-2").css({
        "background-color": "var(--primary)", // Màu xanh lá của bạn
        color: "white",
      });
    });

    $("#backButton").click(function () {
      $(".cart-0, .cart-1, .form-buy").show();
      $(".infor-cart").hide();

      // Remove active class from number-2 and remove animation
      $("#number-2").removeClass("active");
      $("#line-1").removeClass("active-line");
      $("#step-2").removeClass("active-stext");
      // Reset color of number-2 to default state
      $("#number-2").css({
        "background-color": "lightgray", // Màu mặc định của number-2
      });

      
  });


  $(document).ready(function () {
    $("#applyDiscountBtn").click(function () {
      var toast = new bootstrap.Toast(document.getElementById("successToast"));
      toast.show();
    });
  });

  $(document).ready(function () {
    $(".theme-mode input").change(function () {
      if (this.checked) {
        // Chuyển sang dark mode
        $("body").removeClass("light-mode").addClass("dark-mode");
      } else {
        // Chuyển sang light mode
        $("body").removeClass("dark-mode").addClass("light-mode");
      }
    });
  });


})(jQuery);



//Cart Control Begin
const app = angular.module("cart_app", []);

app.controller("ctrl", function ($scope, $http, $timeout) {
  $scope.provinces = [];
  $scope.districts = [];
  $scope.wards = [];
  $scope.selectedProvince = "";
  $scope.selectedDistrict = "";
  $scope.selectedWard = "";
  $scope.result = "";
  $scope.orderDetails = {};
  $scope.listOrder = [];
  $scope.selectedSizeID = "";
  $scope.userAddressDB= "";
  $scope.selectedVoucher = null;
  $scope.numberHouse = "";
  $scope.qrCode = "";
  $scope.ipLocation = "";
  $scope.vehicle = "car";
  $scope.originLocation = encodeURIComponent("10.853832672000067,106.62833998400004");
  $scope.apiKey = "GXxEBBNR5xvIezVsTctnwdM9MznM7HB8bzjCXBvh";
  
  var timeout;

  $scope.changeNameLocationToIP = function () {
    var addressToQuery = $scope.userAddressWeb || $scope.userAddressDB;

    if (addressToQuery) {
    $http
      .get("https://rsapi.goong.io/geocode?address=" + encodeURIComponent(addressToQuery) + "&api_key=" + $scope.apiKey)
      .then(function(response) {
        const location = response.data.results[0].geometry.location;
        $scope.ipLocation = `${location.lat},${location.lng}`;
      });
    }
    if(timeout){
      $timeout.cancel(timeout);
    }

    timeout = $timeout(function(){
        $scope.getDistanceAndCalculateShippingCost();
    },1500);

  };

  $scope.getDistanceAndCalculateShippingCost = function (){
    $http
    .get("https://rsapi.goong.io/DistanceMatrix?origins="+ $scope.originLocation + "&destinations=" + encodeURIComponent($scope.ipLocation) + "&vehicle=" + $scope.vehicle + "&api_key=" + $scope.apiKey)
    .then(function (response) {
      const distance = response.data.rows[0].elements[0].distance;
      $scope.order.distance = parseInt(distance.value);
    });
  };

  $scope.selectOrder = function (orderID) {
    this.selectedOrderId = orderID;
    $http
      .get("/detail/" + this.selectedOrderId)
      .then((response) => {
        if (response.data) {
          $scope.listOrder = response.data;
        }
      })
      .catch((error) => {
      });
  };

  $scope.getSubTotal = function () {
    let subTotal = 0;
    angular.forEach($scope.listOrder, function (orderDetail) {
      $scope.shipCost = orderDetail.distance * 4;
      subTotal += orderDetail.quantity * orderDetail.price;
    });
    return subTotal;
  };

  $scope.getTotal = function () {
    let subTotal = $scope.getSubTotal() + $scope.shipCost;
    return subTotal ;
  };

  $http
    .get("https://provinces.open-api.vn/api/?depth=1")
    .then(function (response) {
      $scope.provinces = response.data;
    });

  $scope.getDistricts = function () {
    if ($scope.selectedProvince) {
      $http
        .get(
          "https://provinces.open-api.vn/api/p/" +
          $scope.selectedProvince +
          "?depth=2"
        )
        .then(function (response) {
          $scope.districts = response.data.districts;
        });
    }
  };

  $scope.getWards = function () {
    if ($scope.selectedDistrict) {
      $http
        .get(
          "https://provinces.open-api.vn/api/d/" +
          $scope.selectedDistrict +
          "?depth=2"
        )
        .then(function (response) {
          $scope.wards = response.data.wards;
        });
    }
  };

  $scope.getSelectedProvinces = function (code) {
    code = parseInt(code);
    for (let i = 0; i < $scope.provinces.length; i++) {
      if ($scope.provinces[i].code === code) {
        return $scope.provinces[i].name;
      }
    }
    return "";
  };

  $scope.getSelectedDistricts = function (code) {
    code = parseInt(code);
    for (let i = 0; i < $scope.districts.length; i++) {
      if ($scope.districts[i].code === code) {
        return $scope.districts[i].name;
      }
    }
    return "";
  };

  $scope.getSelectedWards = function (code) {
    code = parseInt(code);
    for (let i = 0; i < $scope.wards.length; i++) {
      if ($scope.wards[i].code === code) {
        return $scope.wards[i].name;
      }
    }
    return "";
  };

  $scope.printResult = function () {
    if (
      $scope.selectedProvince &&
      $scope.selectedDistrict &&
      $scope.selectedWard
    ) {
      $scope.userAddressWeb =
        $scope.numberHouse +
        "," + " " +
        $scope.getSelectedWards($scope.selectedWard) +
        "," + " " +
        $scope.getSelectedDistricts($scope.selectedDistrict) +
        "," + " " +
        $scope.getSelectedProvinces($scope.selectedProvince);
    }

    if(timeout){
      $timeout.cancel(timeout);
    }

    timeout = $timeout(function(){
        $scope.changeNameLocationToIP();
    },1500);

  };

  function getCart(username) {
    const cartKey = `cart_${username}`;
    const json = localStorage.getItem(cartKey);
    return json
      ? JSON.parse(json)
      : {
        username: username,
        items: [],
      };
  }

  function saveCart(username, cart) {
    let cartKey = `cart_${username}`;
    let json = JSON.stringify(cart);
    localStorage.setItem(cartKey, json);
  }

  function totalPrice() {
    let totalPrice = 0;
    angular.forEach($scope.cart.items, function (item) {
      totalPrice += item.price * item.qty - (item.discount_percent * item.price);
    });
    return totalPrice ;
  }


  $scope.cart = {
    username: "",

    items: [],

    add(id, size_id) {
      if (!this.items) {
        this.items = [];
      }


      let sizeID = parseInt(size_id);

      if (sizeID === null || sizeID === undefined || isNaN(sizeID)) {
        sizeID = 1;
      }

      let item = this.items.find((item) => item.id_product === id && item.id_size === sizeID);


      if (item) {
        item.qty++;
        saveCart(this.username, this);
      } else {
        $http.get(`/rest/products/${id}/${sizeID}`).then((resp) => {
          let newItem = resp.data;
          newItem.qty = 1;
          this.items.push(newItem);
          saveCart(this.username, this);
        });
      }
    },
    remove(id) {
      let index = this.items.findIndex((item) => item.id_product === id);
      this.items.splice(index, 1);
      saveCart(this.username, this);
    },
    clear() {
      this.items = [];
      saveCart(this.username, this);
    },
    get count() {
      if (this.items && this.items.length > 0) {
        return this.items
          .map((item) => item.qty)
          .reduce((total, qty) => (total += qty), 0);
      } else {
        return 0;
      }
    },
    get amount() {
      return totalPrice();
    },
    saveToLocalStorage() {
      let itemsToSave = this.items.map((item) => {
        const { $$hashKey, ...cleanItem } = item;
        return cleanItem;
      });
      saveCart(this.username, itemsToSave);
    },
    loadFromLocalStorage() {
      let cart = getCart(this.username);
      this.items = cart.items;
      $timeout(function () {
        $scope.$apply();
      });
    },

    totalPrice: totalPrice,
  };

  let username = $("#username").text().trim();
  $scope.cart.username = username;
  $scope.cart.loadFromLocalStorage();

  function getCurrentTime() {
    const currentTime = new Date();
    const formattedTime = currentTime.toTimeString().slice(0, 8);
    return formattedTime;
  }

  //Order Begin
  $scope.order = {
    createDate: new Date(),
    address: "",
    id_account: parseInt($("#id_account").text()),
    note: "",
    status: 1,
    totalAmount: $scope.cart.amount,
    createTime: getCurrentTime(),
    id_voucher:"",
    distance:null,

    get orderDetails() {
      return $scope.cart.items.map((item) => {
        return {
          id_product: parseInt(item.id_product),
          price: item.price,
          quantity: item.qty,
          id_size: parseInt(item.id_size),
        };
      });
    },

    purchaseOrder() {
      let order = angular.copy(this);
      $http
        .post(`/rest/order`, order)
        .then((resp) => {
          $scope.cart.clear();
          toastr.success('Order Success');
        })
        .catch((error) => { 
        });
    }
  };


  $scope.handlePaymentMethodChange = function () {
    if ($scope.payment === 1) {
      $scope.order.purchaseOrder();
      $scope.completeButtonClicked();
    } else if ($scope.payment === 2) {
      $scope.order.purchaseOrder();
      location.href = "/vnpay";
    } else if ($scope.payment=== 3) {
      location.href = "/paypal";
      $scope.completeButtonClicked();
    }
  };


  $scope.completeButtonClicked = function () {
      $(".cart-3").show();
      $(".cart-0, .cart-1, .form-buy, .infor-cart").hide();
      $("#number-3").addClass("active");
      $("#line-2").addClass("active-line");
      $("#step-3").addClass("active-text");
  };
  //Order End

  $http.get("/rest/vouchers/all").then(
    function (response) {
      $scope.allVouchers = response.data;
    },
    function (error) {
    }
  );

    $http.get('/rest/vouchers/applicable')
    .then(function(response) {
        $scope.vouchers = response.data;
    }, function(error) {
    });

    $http.get('/rest/order/address')
    .then(function(response) {
        $scope.userAddressDB = response.data.address; 
    }, function(error) {
    });

    $scope.getRemainingTime = function(expireDate) {
        const oneDay = 24 * 60 * 60 * 1000;
        const today = new Date();
        const expiration = new Date(expireDate);

        const difference = expiration - today;
        const daysRemaining = Math.floor(difference / oneDay);

        if (daysRemaining > 0) {
            return "Còn " + daysRemaining + " ngày";
        } else {
            const hoursRemaining = Math.floor((difference % oneDay) / (60 * 60 * 1000));
            const minutesRemaining = Math.floor(((difference % oneDay) % (60 * 60 * 1000)) / (60 * 1000));
            return "Còn " + hoursRemaining + " giờ " + minutesRemaining + " phút";
        }
    };

   $scope.selectVoucher = function(voucher) {
       $scope.selectedVoucher = voucher;
   };

   $scope.applyVoucherNumber = function () {
    let voucherIndex = $scope.allVouchers.findIndex(
      (vou) => vou.number === $scope.voucherNumber
    );

    const voucher = $scope.allVouchers[voucherIndex];

    if (voucherIndex === -1) {
      toastr.error("Voucher doest not exist");
      return;
    }

    if (voucher.status === 2) {
      toastr.error("Voucher is expired");
      return;
    }

    if (voucherIndex !== -1 && voucher.price !== undefined && voucher.status === 1) {
      const discountAmount = voucher.price;
      $scope.cart.totalDiscount = discountAmount;
      $scope.order.id_voucher = voucher.id;
      $scope.order.totalAmount = $scope.order.totalAmount - $scope.cart.totalDiscount;
    }
  };


   $scope.applyVoucher = function() {
      if ($scope.selectedVoucher) {
        const discountAmount = $scope.selectedVoucher.price;
        $scope.cart.totalDiscount = discountAmount;
        $scope.order.id_voucher = parseInt($scope.selectedVoucher.id);
        $scope.order.totalAmount = $scope.order.totalAmount - $scope.cart.totalDiscount;
      }
   };


   $scope.changeAddress = function() {
    if(parseInt($scope.userAddress) === 1){
      $scope.order.address = $scope.userAddressDB;
      $scope.changeNameLocationToIP();
    } else if(parseInt($scope.userAddress) === 2){
      $scope.order.address = $scope.userAddressWeb;
    }
   };
   //Cart Control End


});



