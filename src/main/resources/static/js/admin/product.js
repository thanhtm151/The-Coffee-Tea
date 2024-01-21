const app = angular.module("product_app", []);
app.controller("product_ctrl", function ($scope, $http) {
  $scope.items = [];
  $scope.cates = [];
  $scope.productsizes = [];
  $scope.form = {
    createDate: new Date(),
    image: "icloud-upload.png",
    active: true,
  };

  // Validation
  $scope.validateProduct = function (form) {
      var isValid = true;

      if (form.name && typeof form.name === 'string' && form.name.trim() === '') {
          toastr.warning("Please enter the product name!");
          isValid = false;
      }

      if (form.describe && typeof form.describe === 'string' && form.describe.trim() === '') {
          toastr.warning("Please enter the product description!");
          isValid = false;
      }

       if (form.image === "icloud-upload.png") {
              toastr.warning("Please select an image.");
              isValid = false;
          }

      if (!form.price || form.price <= 0 || isNaN(form.price)) {
          toastr.warning("The product price must be greater than 0 and can only be a number!");
          isValid = false;
      }

      if (form.id_category === "") {
          toastr.warning("Please select a category!");
          isValid = false;
      }

      return isValid;
  };



  $scope.initialize = function () {
    $http.get(`/rest/products`).then((resp) => {
      $scope.items = resp.data;
      $scope.items.forEach((item) => {
        item.createDate = new Date(item.createDate);
      });
    });

    $http.get(`/rest/category`).then((resp) => {
      $scope.cates = resp.data;

      if ($scope.cates.length > 0) {
        $scope.selectedCategory = $scope.cates[0].id;
      } else {
        $scope.selectedCategory = '';
      }
    });

    $http.get(`/rest/productsizes`).then((resp) => {
      $scope.productsizes = resp.data;
    });

    $scope.selectedActive = 'true';
  };

  $scope.initialize();


  $scope.reset = function () {
    $scope.form = {
      createDate: new Date(),
      image: "icloud-upload.png",
      active: true,
    };
  };
  $scope.edit = function (item) {
    $scope.form = angular.copy(item);
  };
  $scope.create = function () {
    if ($scope.validateProduct($scope.form)) {
        let item = angular.copy($scope.form);
        let checkNameProduct = $scope.items.find(product => product.name === item.name);
        if (checkNameProduct) {
            $("#myModal").modal("hide");
            toastr.error("Name already exists");
        } else {
            $http
                .post(`/rest/products`, item)
                .then((resp) => {
                    resp.data.createDate = new Date(resp.data.createDate);
                    $scope.items.push(resp.data);
                    $scope.reset();
                    $("#myModal").modal("hide");
                    toastr.success("Create Success");
                    setTimeout(() => {
                        location.reload();
                    }, 1000);
                })
                .catch((err) => {
                    if (err.data && err.data.errors) {
                        $("#myModal").modal("hide");
                        err.data.errors.forEach(function (error, index) {
                            toastr.error(`Error ${index + 1}: ${error}`);
                        });
                    }
                });
        }
    } else {
        toastr.warning("Please fill in all required fields correctly!");
    }
};


  $scope.update = function () {
   if ($scope.validateProduct($scope.form)){
        let item = angular.copy($scope.form);
        $http.put(`/rest/products/${item.id}`, item).then(resp => {
          let index = $scope.items.findIndex(p => p.id == item.id);
          $scope.items[index] = item;
          $scope.reset();
      $("#myModal").modal("hide");
          toastr.success("Update Success");
      setTimeout(()=>{
       location.reload();
      },1000);
        }).catch(err => {
          if (err.data && err.data.errors) {
        $("#myModal").modal("hide");
        err.data.errors.forEach(function (error, index) {
          toastr.error(`Error ${index + 1}: ${error}`);
        });
      } 
        });
    }
  };

  $scope.deleteItem = null;

  $scope.setDeleteItem = function(item) {
      $scope.deleteItem = item;
  };

  $scope.delete = function (item) {
    if (!item || !item.id) {
      toastr.error("Invalid item or item ID");
      return;
    }

    $http.delete(`/rest/products/${item.id}`).then(resp => {
      let index = $scope.items.findIndex(p => p.id == item.id);
      if (index !== -1) {
        $scope.items.splice(index, 1);
      }
      $scope.reset();
      toastr.success("Delete Success");
    }).catch(err => {
      toastr.error("Delete Fail");
    });
  };

  $scope.selectedImage = null;

  $scope.selectImage = function () {
    document.getElementById("hiddenImageInput").click();
  };
  $scope.imageChanged = function (files) {
    let data = new FormData();
    data.append("file", files[0]);
    $http
      .post(`/rest/upload/img/gallery`, data, {
        transformRequest: angular.identity,
        headers: { "Content-Type": undefined },
      })
      .then((resp) => {
        $scope.form.image = resp.data.name;
      })
      .catch((err) => {
        toastr.error("Select Image Fail");
      });
  };
  // Thêm phương thức để xuất dữ liệu sang Excel
  $scope.exportToExcel = function () {
    $http.get('/export/excel')
      .then(function (response) {
        let blob = new Blob([response.data], { type: 'application/vnd.openxmlformats-officedocument.spreadsheetml.sheet' });
        let downloadLink = document.createElement('a');
        downloadLink.href = window.URL.createObjectURL(blob);
        downloadLink.download = 'product.xlsx';
        downloadLink.click();
        toastr.success("Download Success");
      })
      .catch(function (error) {
        console.error('Error exporting to Excel:', error);
      });
  };

  // Pagination
  $scope.pagedItems = [];
      $scope.pagination = {
          currentPage: 1,
          pageSize: 5,
          totalPages: 0,
          startIndex: 0,
          endIndex: 0,
          pages: []
      };

  $scope.firstPage = function () {
    if ($scope.pagination.currentPage !== 1) {
      $scope.pagination.currentPage = 1;
      paginateItems();
    }
  };

  $scope.lastPage = function () {
    if ($scope.pagination.currentPage !== $scope.pagination.totalPages) {
      $scope.pagination.currentPage = $scope.pagination.totalPages;
      paginateItems();
    }
  };

  $scope.getPagerNumbers = function () {
    let totalPages = $scope.pagination.totalPages;
    let currentPage = $scope.pagination.currentPage;

    if (totalPages <= 5) {
      return Array.from({ length: totalPages }, (_, i) => i + 1);
    } else {
      let startPage = Math.max(1, currentPage - 2);
      let endPage = Math.min(currentPage + 2, totalPages);

      if (endPage - startPage < 4) {
        startPage = Math.max(1, endPage - 4);
      }

      return Array.from({ length: 5 }, (_, i) => startPage + i);
    }
  };

  function paginateItems() {
    var begin = (($scope.pagination.currentPage - 1) * $scope.pagination.pageSize);
    var end = begin + $scope.pagination.pageSize;

    $scope.pagedItems = $scope.filteredItems.slice(begin, end);

    $scope.pagination.startIndex = begin;
    $scope.pagination.endIndex = end > $scope.filteredItems.length ? $scope.filteredItems.length : end;

    $scope.pagination.pages = $scope.getPagerNumbers();
  }


  $scope.$watchGroup(['items.length', 'selectedCategory', 'selectedActive'], function () {
    if ($scope.selectedCategory === '' && $scope.selectedActive === '') {
      $scope.filteredItems = $scope.items;
    } else {
      $scope.filteredItems = $scope.items.filter(function (item) {
        var categoryCondition = $scope.selectedCategory === '' || item.id_category === $scope.selectedCategory;
        var activeCondition = $scope.selectedActive === '' || item.active === ($scope.selectedActive === 'true');
        return categoryCondition && activeCondition;
      });
    }

       $scope.pagination.currentPage = 1;
       paginateItems();
   });

   $scope.$watch('filteredItems.length', function (newValue, oldValue) {
       if (newValue !== oldValue && $scope.filteredItems) {
           $scope.pagination.totalPages = Math.ceil($scope.filteredItems.length / $scope.pagination.pageSize);
           paginateItems();
       }
   });




  $scope.setPage = function (page) {
    if (page < 1 || page > $scope.pagination.totalPages) {
      return;
    }
    $scope.pagination.currentPage = page;
    paginateItems();
  };

  $scope.nextPage = function () {
    if ($scope.pagination.currentPage < $scope.pagination.totalPages) {
      $scope.pagination.currentPage++;
      paginateItems();
    }
  };

  $scope.prevPage = function () {
    if ($scope.pagination.currentPage > 1) {
      $scope.pagination.currentPage--;
      paginateItems();
    }
  };


      // Searching features
      $scope.searchByName = function() {
          if ($scope.searchTerm && $scope.searchTerm.trim() !== '') {
              $http.get(`/rest/productsizes/search?name=${$scope.searchTerm}`)
                  .then(function(response) {
                      $scope.items = response.data;
                      $scope.items.forEach((item) => {
                          item.createDate = new Date(item.createDate);
                      });
                  })
                  .catch(function(error) {
                      console.error('Error searching products:', error);
                  });
          } else {
              $scope.initialize();
          }
      };

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