const app = angular.module("order_app", []);
app.controller("order_ctrl", function ($scope, $http) {
  $scope.listOrders = [];
  $scope.listOrdersConfirmed = [];
  $scope.listOrdersCancelled = [];
  $scope.listOrdersSuccessful = [];
  $scope.listOrderIsShipping = [];
  $scope.orderDetails = {};
  $scope.listOrder = [];
  $scope.status = [];

  $scope.selectedStatusChanged = function () {
    const statusMappings = {
      1: "order",
      2: "order-confirm",
      3: "order-shipping",
      4: "order-success",
      5: "order-cancel",
    };

    Object.values(statusMappings).forEach((id) => {
      document.getElementById(id).style.display = "none";
    });

    const selectedID = statusMappings[$scope.selectedStatus];
    if (selectedID) {
      document.getElementById(selectedID).style.display = "block";
    }
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
      .catch((error) => { });
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

  $scope.initialize = function () {
    if (
      $scope.selectedStatus === null ||
      $scope.selectedStatus === undefined ||
      isNaN($scope.selectedStatus) ||
      $scope.selectedStatus === ""
    ) {
      document.getElementById("order").style.display = "block";
      document.getElementById("order-confirm").style.display = "none";
      document.getElementById("order-shipping").style.display = "none";
      document.getElementById("order-cancel").style.display = "none";
      document.getElementById("order-success").style.display = "none";
    }

    $http.get(`/rest/order`).then((resp) => {
      $scope.listOrders = resp.data;
    });

    $http.get(`/rest/order/status`).then((resp) => {
      $scope.status = resp.data;
      if (!$scope.selectedStatus) {
        $scope.selectedStatus = 1;
        $scope.selectedStatusChanged();
      }
    });

    $http.get(`/rest/order/confirm`).then((resp) => {
      $scope.listOrdersConfirmed = resp.data;
    });

    $http.get(`/rest/order/cancel`).then((resp) => {
      $scope.listOrdersCancelled = resp.data;
    });

    $http.get(`/rest/order/success`).then((resp) => {
      $scope.listOrdersSuccessful = resp.data;
    });

    $http.get(`/rest/order/ship`).then((resp) => {
      $scope.listOrderIsShipping = resp.data;
    });

  };

  $scope.initialize();


  $scope.updateOrder = {
    confirmOrder(orderID) {
      let orderToUpdate = $scope.listOrders.find(function (order) {
        return order.id === orderID;
      });
      if (orderToUpdate) {
        let updateOrder = angular.copy(orderToUpdate);
        updateOrder.status = 2;
        $http
          .put(`/rest/order/${orderToUpdate.id}`, updateOrder)
          .then((resp) => {
            $scope.initialize();
            $scope.selectedStatusChanged(2);
          })
          .catch((err) => { });
      }
    },

    cancelOrder(orderID) {
      let orderToUpdateWhenOrderConfirm = $scope.listOrdersConfirmed.find(
        function (order) {
          return order.id === orderID;
        }
      );

      let orderUpdateWhenOrder = $scope.listOrders.find(function (order) {
        return order.id === orderID;
      });

      let orderUpdateWhenOrderIsShipping = $scope.listOrderIsShipping.find(
        function (order) {
          return order.id === orderID;
        }
      );

      if (orderToUpdateWhenOrderConfirm) {
        let orderCancel = angular.copy(orderToUpdateWhenOrderConfirm);
        orderCancel.status = 5;
        $http
          .put(
            `/rest/order/cancel/${orderToUpdateWhenOrderConfirm.id}`,
            orderCancel
          )
          .then((resp) => {
            $scope.initialize();
            $scope.selectedStatusChanged(5);
          })
          .catch((err) => { });
      } else if (orderUpdateWhenOrder) {
        let orderCancel = angular.copy(orderUpdateWhenOrder);
        orderCancel.status = 5;
        $http
          .put(`/rest/order/cancel/${orderUpdateWhenOrder.id}`, orderCancel)
          .then((resp) => {
            $scope.initialize();
            $scope.selectedStatusChanged(5);
          })
          .catch((err) => { });
      } else if (orderUpdateWhenOrderIsShipping) {
        let orderCancel = angular.copy(orderUpdateWhenOrderIsShipping);
        orderCancel.status = 5;
        $http
          .put(
            `/rest/order/cancel/${orderUpdateWhenOrderIsShipping.id}`,
            orderCancel
          )
          .then((resp) => {
            $scope.initialize();
            $scope.selectedStatusChanged(5);
          })
          .catch((err) => { });
      }
    },
    resetOrder(orderID) {
      let orderToUpdate = $scope.listOrdersCancelled.find(function (order) {
        return order.id === orderID;
      });
      if (orderToUpdate) {
        let orderReset = angular.copy(orderToUpdate);
        orderReset.status = 2;
        $http
          .put(`/rest/order/reset/${orderToUpdate.id}`, orderReset)
          .then((resp) => {
            $scope.initialize();
            $scope.selectedStatusChanged(2);
          })
          .catch((err) => { });
      }
    },
    successOrder(orderID) {
      let orderToUpdate = $scope.listOrdersConfirmed.find(function (order) {
        return order.id === orderID;
      });

      let orderUpdateWhenOrderIsShipping = $scope.listOrderIsShipping.find(
        function (order) {
          return order.id === orderID;
        }
      );

      if (orderToUpdate) {
        let orderSuccess = angular.copy(orderToUpdate);
        orderSuccess.status = 4;
        $http
          .put(`/rest/order/success/${orderToUpdate.id}`, orderSuccess)
          .then((resp) => {
            $scope.initialize();
            $scope.selectedStatusChanged(4);
          })
          .catch((err) => { });
      } else if (orderUpdateWhenOrderIsShipping) {
        let orderSuccess = angular.copy(orderUpdateWhenOrderIsShipping);
        orderSuccess.status = 4;
        $http
          .put(
            `/rest/order/success/${orderUpdateWhenOrderIsShipping.id}`,
            orderSuccess
          )
          .then((resp) => {
            $scope.initialize();
            $scope.selectedStatusChanged(4);
          })
          .catch((err) => { });
      }
    },
    shippingOrder(orderID) {
      let orderToUpdate = $scope.listOrdersConfirmed.find(function (order) {
        return order.id === orderID;
      });

      if (orderToUpdate) {
        let orderIsShipping = angular.copy(orderToUpdate);
        orderIsShipping.status = 3;
        $http
          .put(`/rest/order/ship/${orderToUpdate.id}`, orderIsShipping)
          .then((resp) => {
            $scope.initialize();
            $scope.selectedStatusChanged(3);
          })
          .catch((err) => { });
      }
    },
  };


  // Pending Pagination
  $scope.pagerOrder = {
    currentPage: 1,
    pageSize: 5,
    totalPages: 0,
    startIndex: 0,
    endIndex: 0,
    totalItems: 0,
    pages: []
  };

  function setPageNumbers() {
    $scope.pagerOrder.pages = [];
    for (var i = 1; i <= $scope.pagerOrder.totalPages; i++) {
      $scope.pagerOrder.pages.push(i);
    }
  }

  $scope.setPageOrder = function (page) {
    if (page < 1 || page > $scope.pagerOrder.totalPages) {
      return;
    }
    $scope.pagerOrder.currentPage = page;
    paginateOrder();
  };

  function paginateOrder() {
    var begin = (($scope.pagerOrder.currentPage - 1) * $scope.pagerOrder.pageSize);
    var end = begin + $scope.pagerOrder.pageSize;
    $scope.pagerOrder.paginatedList = $scope.listOrders.slice(begin, end);
    $scope.pagerOrder.startIndex = begin + 1;
    $scope.pagerOrder.endIndex = Math.min(end, $scope.listOrders.length);
    $scope.pagerOrder.totalItems = $scope.listOrders.length;
    $scope.pagerOrder.totalPages = Math.ceil($scope.pagerOrder.totalItems / $scope.pagerOrder.pageSize);
    setPageNumbers();
    $scope.pagerOrder.endIndex = Math.min($scope.pagerOrder.endIndex, $scope.pagerOrder.totalItems);
  }

  $scope.$watch('listOrders', function () {
    paginateOrder();
  });


  // Processing pagination
  $scope.pagerOrderProcessing = {
    currentPage: 1,
    pageSize: 5,
    totalPages: 0,
    startIndex: 0,
    endIndex: 0,
    totalItems: 0,
    pages: []
  };

  function setPageNumbersProcessing() {
    $scope.pagerOrderProcessing.pages = [];
    for (var i = 1; i <= $scope.pagerOrderProcessing.totalPages; i++) {
      $scope.pagerOrderProcessing.pages.push(i);
    }
  }

  $scope.setPageOrderProcessing = function (page) {
    if (page < 1 || page > $scope.pagerOrderProcessing.totalPages) {
      return;
    }
    $scope.pagerOrderProcessing.currentPage = page;
    paginateOrderProcessing();
  };

  function paginateOrderProcessing() {
    var begin = (($scope.pagerOrderProcessing.currentPage - 1) * $scope.pagerOrderProcessing.pageSize);
    var end = begin + $scope.pagerOrderProcessing.pageSize;

    $scope.pagerOrderProcessing.paginatedList = $scope.listOrdersConfirmed.slice(begin, end);

    $scope.pagerOrderProcessing.startIndex = begin + 1;
    $scope.pagerOrderProcessing.endIndex = Math.min(end, $scope.listOrdersConfirmed.length);
    $scope.pagerOrderProcessing.totalItems = $scope.listOrdersConfirmed.length;

    $scope.pagerOrderProcessing.totalPages = Math.ceil($scope.pagerOrderProcessing.totalItems / $scope.pagerOrderProcessing.pageSize);
    setPageNumbersProcessing();

    $scope.pagerOrderProcessing.endIndex = Math.min($scope.pagerOrderProcessing.endIndex, $scope.pagerOrderProcessing.totalItems);
  }

  $scope.$watch('listOrdersConfirmed', function () {
    paginateOrderProcessing();
  });

  // Phân trang cho pagerOrderShipping
  $scope.pagerOrderShipping = {
    currentPage: 1,
    pageSize: 5,
    totalPages: 0,
    startIndex: 0,
    endIndex: 0,
    totalItems: 0,
    pages: []
  };

  function setPageNumbersShipping() {
    $scope.pagerOrderShipping.pages = [];
    for (var i = 1; i <= $scope.pagerOrderShipping.totalPages; i++) {
      $scope.pagerOrderShipping.pages.push(i);
    }
  }

  $scope.setPageOrderShipping = function (page) {
    if (page < 1 || page > $scope.pagerOrderShipping.totalPages) {
      return;
    }
    $scope.pagerOrderShipping.currentPage = page;
    paginateOrderShipping();
  };

  function paginateOrderShipping() {
    var begin = (($scope.pagerOrderShipping.currentPage - 1) * $scope.pagerOrderShipping.pageSize);
    var end = begin + $scope.pagerOrderShipping.pageSize;

    $scope.pagerOrderShipping.paginatedList = $scope.listOrderIsShipping.slice(begin, end);

    $scope.pagerOrderShipping.startIndex = begin + 1;
    $scope.pagerOrderShipping.endIndex = Math.min(end, $scope.listOrderIsShipping.length);
    $scope.pagerOrderShipping.totalItems = $scope.listOrderIsShipping.length;

    $scope.pagerOrderShipping.totalPages = Math.ceil($scope.pagerOrderShipping.totalItems / $scope.pagerOrderShipping.pageSize);
    setPageNumbersShipping();

    $scope.pagerOrderShipping.endIndex = Math.min($scope.pagerOrderShipping.endIndex, $scope.pagerOrderShipping.totalItems);
  }

  $scope.$watch('listOrderIsShipping', function () {
    paginateOrderShipping();
  });


  // page success
  $scope.pagerOrderSuccess = {
    currentPage: 1,
    pageSize: 5,
    totalPages: 0,
    startIndex: 0,
    endIndex: 0,
    totalItems: 0,
    pages: []
  };

  function setPageNumbersSuccess() {
    $scope.pagerOrderSuccess.pages = [];
    for (var i = 1; i <= $scope.pagerOrderSuccess.totalPages; i++) {
      $scope.pagerOrderSuccess.pages.push(i);
    }
  }

  $scope.setPageOrderSuccess = function (page) {
    if (page < 1 || page > $scope.pagerOrderSuccess.totalPages) {
      return;
    }
    $scope.pagerOrderSuccess.currentPage = page;
    paginateOrderSuccess();
  };

  function paginateOrderSuccess() {
    var begin = (($scope.pagerOrderSuccess.currentPage - 1) * $scope.pagerOrderSuccess.pageSize);
    var end = begin + $scope.pagerOrderSuccess.pageSize;
    $scope.pagerOrderSuccess.paginatedList = $scope.listOrdersSuccessful.slice(begin, end);
    $scope.pagerOrderSuccess.startIndex = begin + 1;
    $scope.pagerOrderSuccess.endIndex = Math.min(end, $scope.listOrdersSuccessful.length);
    $scope.pagerOrderSuccess.totalItems = $scope.listOrdersSuccessful.length;
    $scope.pagerOrderSuccess.totalPages = Math.ceil($scope.pagerOrderSuccess.totalItems / $scope.pagerOrderSuccess.pageSize);
    setPageNumbersSuccess();
    $scope.pagerOrderSuccess.endIndex = Math.min($scope.pagerOrderSuccess.endIndex, $scope.pagerOrderSuccess.totalItems);
  }

  $scope.$watch('listOrdersSuccessful', function () {
    paginateOrderSuccess();
  });


  // Pagination cancel
  $scope.pagerOrderCancel = {
    currentPage: 1,
    pageSize: 5,
    totalPages: 0,
    startIndex: 0,
    endIndex: 0,
    totalItems: 0,
    pages: []
  };

  function setPageNumbersCancel() {
    $scope.pagerOrderCancel.pages = [];
    for (var i = 1; i <= $scope.pagerOrderCancel.totalPages; i++) {
      $scope.pagerOrderCancel.pages.push(i);
    }
  }

  $scope.setPageOrderCancel = function (page) {
    if (page < 1 || page > $scope.pagerOrderCancel.totalPages) {
      return;
    }
    $scope.pagerOrderCancel.currentPage = page;
    paginateOrderCancel();
  };

  function paginateOrderCancel() {
    var begin = (($scope.pagerOrderCancel.currentPage - 1) * $scope.pagerOrderCancel.pageSize);
    var end = begin + $scope.pagerOrderCancel.pageSize;
    $scope.pagerOrderCancel.paginatedList = $scope.listOrdersCancelled.slice(begin, end);
    $scope.pagerOrderCancel.startIndex = begin + 1;
    $scope.pagerOrderCancel.endIndex = Math.min(end, $scope.listOrdersCancelled.length);
    $scope.pagerOrderCancel.totalItems = $scope.listOrdersCancelled.length;
    $scope.pagerOrderCancel.totalPages = Math.ceil($scope.pagerOrderCancel.totalItems / $scope.pagerOrderCancel.pageSize);
    setPageNumbersCancel();
    $scope.pagerOrderCancel.endIndex = Math.min($scope.pagerOrderCancel.endIndex, $scope.pagerOrderCancel.totalItems);
  }

  $scope.$watch('listOrdersCancelled', function () {
    paginateOrderCancel();
  });

  // Searching features
  $scope.searchByStatusAndUsername = function () {
    $http.get(`/rest/order/searchByStatusAndUsername?statusID=${$scope.selectedStatus}&username=${$scope.searchKeyword}`)
      .then((resp) => {
        $scope.listOrders = resp.data;
      })
      .catch((error) => {

      });
  };



});


// Nhấn mạnh là thg Thành n làm
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

