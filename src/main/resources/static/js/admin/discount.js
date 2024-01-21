const app = angular.module("discount-app", []);
app.controller("discount-ctrl", function ($scope, $http) {

  $scope.discounts = [];
  $scope.cates = [];

  $scope.initialize = function () {
    $http.get(`/rest/discount`).then((resp) => {
      $scope.discounts = resp.data;
      $scope.discounts.forEach((count) => {
        count.activeDate = new Date(count.activeDate);
        count.expiredDate = new Date(count.expiredDate);
      });
    });
    $http.get(`/rest/category`).then((resp) => {
      $scope.cates = resp.data;
    });

    $scope.form = {
      active: true,
      activeDate: new Date()
    };
  };

  $scope.initialize();

    $scope.reset = function () {
       $scope.form = {
        active:true,
       };
     };
     $scope.edit = function (count) {
       $scope.form = angular.copy(count);
     };

     // validation
     $scope.validateDiscount = function(form) {
         var isValid = true;

         if (!form.name || !form.number) {
             toastr.warning("Please enter both Discount Name and Discount Number!");
             isValid = false;
         }

         if (isNaN(form.percent) || form.percent <= 0 || form.percent >= 1) {
             toastr.warning("Discount Percent must be a number greater than 0 and less than 1!");
             isValid = false;
         }

         if (!form.idcategory) {
             toastr.warning("Please select a category!");
             isValid = false;
         }

         if (form.expiredDate <= form.activeDate) {
             toastr.warning("Expired Date must be greater than Active Date!");
             isValid = false;
         }

         return isValid;
     };

   $scope.create = function() {
       if ($scope.validateDiscount($scope.form)) {
           let checkNameDiscount = $scope.discounts.find(dis => dis.name === $scope.form.name);
           if (checkNameDiscount) {
               $("#discountModal").modal("hide");
               toastr.error("Name already exists");
           } else {
               $http.post(`/rest/discount`, $scope.form)
                   .then((resp) => {
                       resp.data.activeDate = new Date(resp.data.activeDate);
                       resp.data.expiredDate = new Date(resp.data.expiredDate);
                       $scope.discounts.push(resp.data);
                       $scope.reset();
                       $("#discountModal").modal("hide");
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
           }
       } else {
           toastr.warning("Please fill in all required fields correctly!");
       }
   };

   $scope.update = function() {
       if ($scope.validateDiscount($scope.form)) {
           let checkNameDiscount = $scope.discounts.find(dis => dis.name === $scope.form.name && dis.id !== $scope.form.id);
           if (checkNameDiscount) {
               $("#discountModal").modal("hide");
               toastr.error("Name already exists");
           } else {
               $http.put(`/rest/discount/${$scope.form.id}`, $scope.form)
                   .then((resp) => {
                       let index = $scope.discounts.findIndex(p => p.id === $scope.form.id);
                       $scope.discounts[index] = resp.data;
                       $scope.reset();
                       $("#discountModal").modal("hide");
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
           }
       } else {
           toastr.warning("Please fill in all required fields correctly!");
       }
   };


   $scope.updateApplyDiscount = function(categoryID, discountID,categoryName) {
    let category = {
      id: categoryID,
      id_discount: discountID,
      name: categoryName,
    };
    $http.put(`/rest/category/update/${categoryID}`, category).then(resp => {
      let index = $scope.cates.findIndex((cate) => cate.id === categoryID);
      $scope.cates[index] = category;
      $scope.reset();
      toastr.success("Update Success");
      setTimeout(()=>{
       location.reload();
      },1000);
    }).catch(err => {
      toastr.error("Update Fail");
    })
 };


   $scope.deleteItem = null;

   $scope.setDeleteItem = function(item) {
       $scope.deleteItem = item;
   };
 

   $scope.delete = function (count) {
    if (!count || !count.id) {
      toastr.error("Invalid item or item ID");
      return;
    }

    $http.delete(`/rest/discount/${count.id}`).then(resp => {
      let index = $scope.discounts.findIndex(p => p.id === count.id);
      $scope.discounts.splice(index, 1);
      $scope.reset();
      $("#discountModal").modal("hide");
      toastr.success("Delete Success");
      setTimeout(()=>{
     location.reload();
    },1000);
    }).catch(err => {
      toastr.error("Create Fail");
    })
  };
        $scope.updateNotApplyDiscount = function(categoryID, categoryName) {
          let category = {
            id: categoryID,
            id_discount: '',
            name: categoryName,
          };
          $http.put(`/rest/category/update/${categoryID}`, category).then(resp => {
            let index = $scope.cates.findIndex((cate) => cate.id === categoryID);
            $scope.cates[index] = category;
            $scope.reset();
            toastr.success("Update Success");
            setTimeout(()=>{
             location.reload();
            },1000);
          }).catch(err => {
            toastr.error("Update Fail");
          })
       };
      
       $scope.createCategory = function (name) {
        let checkNameCategory = $scope.cates.find(c => c.name === name);

        if(checkNameCategory){
          $("#createModal").modal("hide");
          toastr.error("Name already exist");
        } else {
          let category = {
            name: name,
          };
          $http
            .post(`/rest/category`, category)
            .then((resp) => {
              $scope.cates.push(resp.data);
              $("#createModal").modal("hide");
              toastr.success("Create Success");
              setTimeout(()=>{
               location.reload();
              },1000);
            })
            .catch((err) => { 
              if (err.data && err.data.errors) {
                $("#createModal").modal("hide");
                toastr.error(err.data.errors);
              } 
            });
        }

      };

        // Pagination
        $scope.currentPageDiscount = 1; // Trang hiện tại
        $scope.pageSizeDiscount = 5; // Số lượng discount mỗi trang

        $scope.totalPagesDiscount = function () {
            return Math.ceil($scope.discounts.length / $scope.pageSizeDiscount);
        };

        $scope.setPageDiscount = function (page) {
            if (page >= 1 && page <= $scope.totalPagesDiscount()) {
                $scope.currentPageDiscount = page;
            }
        };

  $scope.firstPageDiscount = function () {
    if ($scope.currentPageDiscount !== 1) {
      $scope.currentPageDiscount = 1;
    }
  };

  $scope.lastPageDiscount = function () {
    if ($scope.currentPageDiscount !== $scope.totalPagesDiscount()) {
      $scope.currentPageDiscount = $scope.totalPagesDiscount();
    }
  };

  $scope.getPagerDiscount = function () {
    const totalPages = $scope.totalPagesDiscount(); // Tổng số trang
    const maxPagesToShow = 5; // Số trang tối đa cần hiển thị

    let startPage = 1;
    let endPage = totalPages;

    if (totalPages > maxPagesToShow) {
      const maxPagesBeforeCurrentPage = Math.floor(maxPagesToShow / 2);
      const maxPagesAfterCurrentPage = Math.ceil(maxPagesToShow / 2) - 1;

      if ($scope.currentPageDiscount <= maxPagesBeforeCurrentPage) {
        startPage = 1;
        endPage = maxPagesToShow;
      } else if ($scope.currentPageDiscount + maxPagesAfterCurrentPage >= totalPages) {
        startPage = totalPages - maxPagesToShow + 1;
        endPage = totalPages;
      } else {
        startPage = $scope.currentPageDiscount - maxPagesBeforeCurrentPage;
        endPage = $scope.currentPageDiscount + maxPagesAfterCurrentPage;
      }
    }

    return Array.from({ length: endPage - startPage + 1 }, (_, i) => startPage + i);
  };

  $scope.paginatedListDiscount = function () {
    const begin = ($scope.currentPageDiscount - 1) * $scope.pageSizeDiscount;
    const end = begin + $scope.pageSizeDiscount;
    return $scope.discounts.slice(begin, end);
  };


  $scope.searchDiscount = function () {
    if ($scope.searchText) {
      $http.get("/rest/discount/search?name=" + $scope.searchText)
        .then(function (response) {
          $scope.discounts = response.data;
        })
        .catch(function (error) {
          console.error("Error fetching discounts:", error);
        });
    } else {
      $scope.initialize();
    }
  };

          $scope.$watch('searchText', function(newVal, oldVal) {
            if (newVal !== oldVal) {
              $scope.searchDiscount();
            }
          });


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

