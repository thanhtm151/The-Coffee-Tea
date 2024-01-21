let voucherApp = angular.module("voucher_app", []);

voucherApp.controller("voucher_ctrl", function ($scope, $http) {
  $scope.vouchers = [];
  $scope.status = [];
  $scope.types = [];
  $scope.defaultStatus = "";
  $scope.selectedStatus = $scope.defaultStatus;
  $scope.searchText = "";

  $scope.voucher = {
    type: 1,
    status: 1,
  };

  $scope.initialize = function () {
    $http.get("/rest/vouchers/all").then(
      function (response) {
        $scope.vouchers = response.data;
      },
      function (error) {
        console.error("Error fetching vouchers:", error);
      }
    );

    $http.get("/rest/vouchers/voucherstatus/all").then(
      function (response) {
        $scope.status = response.data;
      },
      function (error) {
        console.error("Error fetching voucher status:", error);
      }
    );

    $http.get("/rest/vouchers/type/all").then(
      function (response) {
        $scope.types = response.data;
      },
      function (error) {
        console.error("Error fetching voucher status:", error);
      }
    );
  };
  $scope.initialize();

  $scope.filterVouchers = function () {
    if ($scope.selectedStatus === "") {
      $http.get("/rest/vouchers/all").then(
        function (response) {
          $scope.vouchers = response.data;
        },
        function (error) {
          console.error("Error fetching vouchers:", error);
        }
      );
    } else {
      $http.get("/rest/vouchers/filterByStatus/" + $scope.selectedStatus).then(
        function (response) {
          $scope.vouchers = response.data;
        },
        function (error) {
          console.error("Error fetching filtered vouchers:", error);
        }
      );
    }
  };

  $scope.searchVouchers = function () {
    $http.get("/rest/vouchers/search?name=" + $scope.searchText).then(
      function (response) {
        $scope.vouchers = response.data;
      },
      function (error) {
        console.error("Error fetching vouchers:", error);
      }
    );
  };

  $scope.setVoucherToDelete = function (voucher) {
    $scope.voucherToDelete = voucher;
  };

  $scope.reset = function () {
    $scope.form = {
      icon: "bi bi-cup-straw"
    };
    $("#myModal").show("hide");
    // $scope.selectedIconClass
  };

  $scope.validateVoucher = function() {
      let isValid = true;

        if (!$scope.voucher.icon) {
          toastr.warning("Please select an Icon for the voucher!");
          isValid = false;
      }

      if (!$scope.voucher.name || !$scope.voucher.number) {
          toastr.warning("Please enter both Name and Number for the voucher!");
          isValid = false;
      }

      if (isNaN($scope.voucher.price) || $scope.voucher.price <= 0) {
          toastr.warning("Voucher Price must be a number greater than 0!");
          isValid = false;
      }

      if (isNaN($scope.voucher.condition) || $scope.voucher.condition <= $scope.voucher.price) {
          toastr.warning("Condition must be a number greater than Voucher Price!");
          isValid = false;
      }

      if (!$scope.voucher.expiredDate) {
          toastr.warning("Please select an Expiry Date for the voucher!");
          isValid = false;
      }

      if (!$scope.voucher.type) {
          toastr.warning("Please select a Type for the voucher!");
          isValid = false;
      }

      if (!$scope.voucher.status || ($scope.voucher.status !== 1 && $scope.voucher.status !== 2)) {
          toastr.warning("Please select the Status for the voucher!");
          isValid = false;
      }

      return isValid;
  };


  $scope.voucher = {
    name: "",
    number: "",
    createDate: new Date(),
    expiredDate:"",
    price:"",
    condition:"",
    icon:"",
    status:"",
    id_account: "",
    type: "",

    createVoucher() {
      if ($scope.validateVoucher()) {
          let voucher = angular.copy($scope.voucher);
          $http.post(`/rest/vouchers`, voucher)
          .then((resp) => {
              $scope.vouchers.push(resp.data);
              $scope.reset();
              toastr.success("Create Success");
              setTimeout(() => {
                  location.reload();
                  }, 1000);
              })
              .catch((err) => {
                  if (err.data && err.data.errors) {
                      $("#discountModal").modal("hide");
                      err.data.errors.forEach(function(error, index) {
                          toastr.error(`Error ${index + 1}: ${error}`);
                      });
                  }
              });
          } else {
              toastr.warning("Please fill in all required fields correctly!");
          }
      }
  };

  $scope.editVoucher = function (voucher) {
    $scope.voucher = angular.copy(voucher);
    $scope.voucher.expiredDate = new Date(voucher.expiredDate);
  };

  $scope.updateVoucher = function() {
      if ($scope.validateVoucher()) {
          // Thực hiện cập nhật voucher khi thông tin hợp lệ
          let voucher = angular.copy($scope.voucher);
          $http.put(`/rest/vouchers/${voucher.id}`, voucher)
              .then((resp) => {
                  let index = $scope.vouchers.findIndex((v) => v.id == voucher.id);
                  $scope.vouchers[index] = voucher;
                  $scope.reset();
                  toastr.success("Update Success");
                  setTimeout(() => {
                      location.reload();
                  }, 1000);
              })
              .catch((err) => {
                  if (err.data && err.data.errors) {
                      $("#discountModal").modal("hide");
                      err.data.errors.forEach(function(error, index) {
                          toastr.error(`Error ${index + 1}: ${error}`);
                      });
                  }
              });
      } else {
          toastr.warning("Please fill in all required fields correctly!");
      }
  };

  $scope.updateListVoucher = function() {
      if ($scope.validateVoucher()) {
          // Thực hiện cập nhật danh sách voucher khi thông tin hợp lệ
          let voucher = angular.copy($scope.voucher);
          $http.put(`/rest/vouchers/${voucher.name}/${voucher.type}`, voucher)
              .then((resp) => {
                  let index = $scope.vouchers.findIndex((v) => v.name === voucher.name && v.type === voucher.type);
                  $scope.vouchers[index] = voucher;
                  $scope.reset();
                  toastr.success("Update Success");
                  setTimeout(() => {
                      location.reload();
                  }, 1000);
              })
              .catch((err) => {
                  toastr.error("Update Fail");
              });
      } else {
          toastr.warning("Please fill in all required fields correctly!");
      }
  };

  $scope.deleteItem = null;

  $scope.setDeleteItem = function(item) {
      $scope.deleteItem = item;
  };

  // Delete voucher
  $scope.deleteVoucher = function (voucher) {
    if (!voucher || !voucher.id) {
      toastr.error("Invalid item or item ID");
      return;
    }
    console.log(voucher.id);
    $http.delete(`/rest/vouchers/${voucher.id}`).then(resp => {
      let index = $scope.vouchers.findIndex(vou => vou.id === voucher.id);
      $scope.vouchers.splice(index, 1);
      toastr.success("Delete Success");
      setTimeout(() => {
        location.reload();
      }, 1000);
    }).catch(err => {
      toastr.success("Delete Success");
      setTimeout(()=>{
        location.reload();
      },1000);
    });
  };

  $scope.deleteListVoucherByNameAndIdType = function (number, idType) {
    $http
      .delete("/rest/vouchers/" + number + "/" + idType)
      .then(function (response) {
        $scope.vouchers = $scope.vouchers.filter(function (voucher) {
          return voucher.number !== number && voucher.type !== type;
        });
        toastr.success("Xóa voucher thành công");

        setTimeout(() => {
          location.reload();
        }, 1000);
      })
      .catch((error) => { 
        toastr.success("Xóa voucher thành công");

        setTimeout(() => {
          location.reload();
        }, 1000);
      });
  };

  // Pagination
  $scope.currentPageVoucher = 1;
  $scope.pageSizeVoucher = 5;
  $scope.maxPagesToShow = 5; // Số trang tối đa được hiển thị

  $scope.totalPagesVoucher = function () {
    return Math.ceil($scope.vouchers.length / $scope.pageSizeVoucher);
  };

  $scope.setPageVoucher = function (page) {
    if (page >= 1 && page <= $scope.totalPagesVoucher()) {
      $scope.currentPageVoucher = page;
    }
  };

  $scope.paginatedListVoucher = function () {
    const begin = ($scope.currentPageVoucher - 1) * $scope.pageSizeVoucher;
    const end = begin + $scope.pageSizeVoucher;

    return $scope.vouchers.slice(begin, end);
  };

  $scope.firstPageVoucher = function () {
    if ($scope.currentPageVoucher !== 1) {
      $scope.currentPageVoucher = 1;
    }
  };

  $scope.lastPageVoucher = function () {
    const totalPages = $scope.totalPagesVoucher();
    if ($scope.currentPageVoucher !== totalPages) {
      $scope.currentPageVoucher = totalPages;
    }
  };

  $scope.getPager = function () {
    const totalPages = $scope.totalPagesVoucher();
    let startPage = 1;
    let endPage = totalPages;

    if (totalPages > $scope.maxPagesToShow) {
      const maxPagesBeforeCurrentPage = Math.floor($scope.maxPagesToShow / 2);
      const maxPagesAfterCurrentPage = Math.ceil($scope.maxPagesToShow / 2) - 1;

      if ($scope.currentPageVoucher <= maxPagesBeforeCurrentPage) {
        startPage = 1;
        endPage = $scope.maxPagesToShow;
      } else if ($scope.currentPageVoucher + maxPagesAfterCurrentPage >= totalPages) {
        startPage = totalPages - $scope.maxPagesToShow + 1;
        endPage = totalPages;
      } else {
        startPage = $scope.currentPageVoucher - maxPagesBeforeCurrentPage;
        endPage = $scope.currentPageVoucher + maxPagesAfterCurrentPage;
      }
    }

    return Array.from({ length: endPage - startPage + 1 }, (_, i) => startPage + i);
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


