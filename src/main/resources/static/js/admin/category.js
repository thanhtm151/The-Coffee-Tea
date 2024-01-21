const app = angular.module("category-app", []);
app.controller("category-ctrl", function ($scope, $http) {
  $scope.cates = [];
  $scope.form = {};
  $scope.initialize = function () {
    $http.get(`/rest/category`).then((resp) => {
      $scope.cates = resp.data;
    });
  };
  $scope.initialize();
  //category
  $scope.edit = function (cate) {
    $scope.form = angular.copy(cate);
  };
  $scope.clearForm = function () {
    $scope.form = {};
  };
  $scope.create = function () {
    let cate = angular.copy($scope.form);
    let name = $scope.cates.find(function (category) {
      return category.name === cate.name;
    });
    if (name) {
      alert("Category is exist");
    } else {
      $http
        .post(`/rest/category`, cate)
        .then(function (response) {
          $scope.clearForm();
          alert("Create Successful");
        })
        .catch(function (error) { });
    }
  };
  $scope.update = function () {
    let cate = angular.copy($scope.form);
    $http
      .put(`/rest/category/update/${cate.id}`, cate)
      .then((resp) => {
        let index = $scope.cates.findIndex((p) => p.id == cate.id);
        $scope.cates[index] = cate;
        $scope.clearForm();
        alert("Update Success");
      })
      .catch((err) => { });
  };
  $scope.delete = function (cate) {
    $http
      .delete(`/rest/category/delete/${cate.id}`)
      .then((resp) => {
        let index = $scope.cates.findIndex((c) => c.id == cate.id);
        $scope.cates.splice(index, 1);
        $scope.clearForm();
        alert("Delete Success");
      })
      .catch((err) => { });
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


