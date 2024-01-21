const app = angular.module("authority-app", []);
app.controller("authority-ctrl", function ($scope, $http, $location, $timeout) {
  $scope.roles = [];
    $scope.listStaff = [];
    $scope.authorities = [];
    $scope.staff = [];

    var timeout;

    $scope.initialize = function () {
      $http.get(`/rest/roles`).then((resp) => {
        $scope.roles = resp.data;
      });

      $http.get(`/rest/staff/admin?admin=true`).then((resp) => {
        $scope.listStaff = resp.data;
      });

      $http
        .get(`/rest/authorities?admin=true`)
        .then((resp) => {
          $scope.authorities = resp.data;
        })
        .catch((error) => {
          $location.path(`/login/unauthorized`);
        });

      $scope.avatar = "default.png";
    };
    $scope.initialize();

    $scope.authority_of = function (acc, role) {
      if ($scope.authorities) {
        return $scope.authorities.find(
          (ur) => ur.account.username == acc.username && ur.role.id == role.id
        );
      }
    };

    $scope.authority_changed = function (acc, role) {
      let authority = $scope.authority_of(acc, role);
      if (authority) {
        $scope.revoke_authority(authority);
      } else {
        authority = { account: acc, role: role };
        $scope.grant_authority(authority);
      }
    };

    $scope.grant_authority = function (authority) {
      $http
        .post(`/rest/authorities`, authority)
        .then((resp) => {
          $scope.authorities.push(resp.data);
          toastr.success("Authorization successful");
          setTimeout(()=>{
            location.reload();
          },1000);
        })
        .catch((error) => {
          toastr.error("Authorization Fail");
        });
    };

    $scope.revoke_authority = function (authority) {
      $http
        .delete(`/rest/authorities/${authority.id}`, authority)
        .then((resp) => {
          let index = $scope.authorities.findIndex((a) => a.id == authority.id);
          $scope.authorities.splice(index, 1);
          toastr.success("Permissions revoked successfully");
          setTimeout(()=>{
            location.reload();
          },1000);
        })
        .catch((err) => {
          toastr.error("Permissions revoked Fail");
        });
    };

    $scope.clearForm = function () {
      $scope.username = "";
      $scope.firstname = "";
      $scope.lastname = "";
      $scope.password = "";
      $scope.email = "";
      $scope.phone = "";
      $scope.avatar = "default.png";
      $scope.address = "";
    };

    // Validations
    $scope.validateStaff = function () {
        var isValid = true;

        if (!$scope.username || !$scope.firstname || !$scope.lastname || !$scope.address || !$scope.password || !$scope.email || !$scope.phone || !$scope.avatar) {
            toastr.warning("Please fill in all fields.");
            isValid = false;
        }

        const passwordRegex = /^(?=.*?[A-Z])(?=.*?[a-z])(?=.*?[0-9])(?=.*?[!@#$%^&*()_+{}\[\]:;<>,.?~]).{8,}$/;
        if (!passwordRegex.test($scope.password)) {
            toastr.warning("Password must be at least 8 characters long, include at least 1 uppercase letter, 1 lowercase letter, 1 digit, and 1 special character.");
            isValid = false;
        }

        const emailRegex = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
        if (!emailRegex.test($scope.email)) {
            toastr.warning("Please enter a valid email address.");
            isValid = false;
        }

        const phoneRegex = /^[0-9]{10}$/;
        if (!phoneRegex.test($scope.phone)) {
            toastr.warning("Please enter a valid phone number (10 digits).");
            isValid = false;
        }

        if ($scope.avatar === "default.png") {
            toastr.warning("Please select an image.");
            isValid = false;
        }

        return isValid;
    };


    $scope.saveAccount = function () {
        if ($scope.validateStaff()) {
            let account = {
                username: $scope.username,
                fullname: $scope.firstname + " " + $scope.lastname,
                password: $scope.password,
                email: $scope.email,
                phone: $scope.phone,
                firstname: $scope.firstname,
                lastname: $scope.lastname,
                avatar: $scope.avatar,
                address: $scope.address,
            };
            let existUsername = $scope.listStaff.find(function (staff) {
                return staff.username === account.username;
            });
            if (existUsername) {
                toastr.warning("Username already exists.");
            } else {
                $http.post(`/rest/staff/add`, account)
                    .then(function (response) {
                        $scope.listStaff.push(response.data);
                        $scope.clearForm();
                        toastr.success("Create Successful");
                        setTimeout(() => {
                            location.reload();
                        }, 1000);
                    })
                    .catch(function (err) {
                        toastr.error("Create Fail");
                    });
            }
        } else {
            toastr.error("Please fill in all required fields with valid data.");
        }
    };

    $scope.selectedImage = null;
    $scope.selectImage = function () {
      document.getElementById("avatar").click();
    };
    $scope.imageChanged = function (files) {
      let data = new FormData();
      data.append("file", files[0]);
      $http
        .post(`/rest/upload/img/avatar`, data, {
          transformRequest: angular.identity,
          headers: { "Content-Type": undefined },
        })
        .then((resp) => {
          $scope.avatar = resp.data.name;
        })
        .catch((err) => {
          toastr.error("Select image Fail");
          console.log(err);
        });
    };

    $scope.getInformation = function(account) {
      $scope.form = angular.copy(account);
    };

    $scope.autoCompleteAddress = function () {
      if(timeout){
        $timeout.cancel(timeout);
      }
  
      timeout = $timeout(function(){
          $http.get(`https://rsapi.goong.io/Place/AutoComplete?api_key=GXxEBBNR5xvIezVsTctnwdM9MznM7HB8bzjCXBvh&input=` + encodeURIComponent($scope.address)).then((resp) => {
            $scope.address = resp.data.predictions[0].description;
          });
      },2000);
    };

  // Pagination

  $scope.currentPageAuth = 1;
  $scope.pageSizeAuth = 5;

  $scope.totalPagesAuth = function () {
    return Math.ceil($scope.listStaff.length / $scope.pageSizeAuth);
  };

  $scope.setPageAuth = function (page) {
    if (page >= 1 && page <= $scope.totalPagesAuth()) {
      $scope.currentPageAuth = page;
    }
  };

  $scope.firstPageAuth = function () {
    if ($scope.currentPageAuth !== 1) {
      $scope.currentPageAuth = 1;
    }
  };

  $scope.lastPageAuth = function () {
    if ($scope.currentPageAuth !== $scope.totalPagesAuth()) {
      $scope.currentPageAuth = $scope.totalPagesAuth();
    }
  };

  $scope.getPagerAuth = function () {
    const totalPages = $scope.totalPagesAuth();
    const currentPage = $scope.currentPageAuth;
    const maxPagesToShow = 5;

    let startPage, endPage;
    if (totalPages <= maxPagesToShow) {
      startPage = 1;
      endPage = totalPages;
    } else {
      if (currentPage <= Math.ceil(maxPagesToShow / 2)) {
        startPage = 1;
        endPage = maxPagesToShow;
      } else if (currentPage + Math.floor(maxPagesToShow / 2) >= totalPages) {
        startPage = totalPages - maxPagesToShow + 1;
        endPage = totalPages;
      } else {
        startPage = currentPage - Math.floor(maxPagesToShow / 2);
        endPage = currentPage + Math.floor(maxPagesToShow / 2);
      }
    }

    const pages = Array.from(Array(endPage + 1 - startPage).keys()).map(i => startPage + i);
    return pages;
  };

  $scope.paginatedListAuth = function () {
    const begin = ($scope.currentPageAuth - 1) * $scope.pageSizeAuth;
    const end = begin + $scope.pageSizeAuth;

    return $scope.listStaff.slice(begin, end);
  };

  // Trang thứ hai (pagination cho trang nhân viên)
  $scope.currentPage = 1;
  $scope.pageSize = 5; // Số nhân viên hiển thị mỗi trang

  $scope.totalPages = function () {
    return Math.ceil($scope.listStaff.length / $scope.pageSize);
  };

  $scope.setPage = function (page) {
    if (page >= 1 && page <= $scope.totalPages()) {
      $scope.currentPage = page;
    }
  };

  $scope.firstPage = function () {
    if ($scope.currentPage !== 1) {
      $scope.currentPage = 1;
    }
  };

  $scope.lastPage = function () {
    if ($scope.currentPage !== $scope.totalPages()) {
      $scope.currentPage = $scope.totalPages();
    }
  };

  $scope.getPager = function () {
    const totalPages = $scope.totalPages();
    const currentPage = $scope.currentPage;
    const maxPagesToShow = 5;

    let startPage, endPage;
    if (totalPages <= maxPagesToShow) {
      startPage = 1;
      endPage = totalPages;
    } else {
      if (currentPage <= Math.ceil(maxPagesToShow / 2)) {
        startPage = 1;
        endPage = maxPagesToShow;
      } else if (currentPage + Math.floor(maxPagesToShow / 2) >= totalPages) {
        startPage = totalPages - maxPagesToShow + 1;
        endPage = totalPages;
      } else {
        startPage = currentPage - Math.floor(maxPagesToShow / 2);
        endPage = currentPage + Math.floor(maxPagesToShow / 2);
      }
    }

    const pages = Array.from(Array(endPage + 1 - startPage).keys()).map(i => startPage + i);
    return pages;
  };

  $scope.paginatedList = function () {
    const begin = ($scope.currentPage - 1) * $scope.pageSize;
    const end = begin + $scope.pageSize;

    return $scope.listStaff.slice(begin, end);
  };

  // Pagination for user accounts
  $scope.currentPage = 1;
  $scope.pageSize = 5; // Số nhân viên hiển thị mỗi trang

  $scope.totalPages = function () {
    return Math.ceil($scope.listStaff.length / $scope.pageSize);
  };

  $scope.setPage = function (page) {
    if (page >= 1 && page <= $scope.totalPages()) {
      $scope.currentPage = page;
    }
  };

  $scope.paginatedList = function () {
    const begin = ($scope.currentPage - 1) * $scope.pageSize;
    const end = begin + $scope.pageSize;
    return $scope.listStaff.slice(begin, end);
  };

  $scope.nextPage = function () {
    if ($scope.currentPage < $scope.totalPages()) {
      $scope.currentPage++;
    }
  };

  $scope.prevPage = function () {
    if ($scope.currentPage > 1) {
      $scope.currentPage--;
    }
  };



  $scope.initialize();

  $scope.getInformation = function (account) {
    $scope.form = angular.copy(account);
  };

  $scope.autoCompleteAddress = function () {
    if (timeout) {
      $timeout.cancel(timeout);
    }

    timeout = $timeout(function () {
      $http.get(`https://rsapi.goong.io/Place/AutoComplete?api_key=GXxEBBNR5xvIezVsTctnwdM9MznM7HB8bzjCXBvh&input=` + encodeURIComponent($scope.address)).then((resp) => {
        $scope.address = resp.data.predictions[0].description;
      });
    }, 2000);
  };


  // Unban account features
  $scope.confirmUnlock = function (account) {
    $scope.selectedAccount = account;
    $('#confirmUnlockModal').modal('show');
  };

  $scope.unlockAccount = function (selectedAccount) {
    $http.put('/rest/profile/unlock/' + selectedAccount.id)
      .then(function (response) {
        toastr.success('Tài khoản đã được mở khóa thành công.');
        $('#confirmUnlockModal').modal('hide');
        location.reload();
      })
      .catch(function (error) {
        toastr.error('Mở khóa tài khoản thất bại.');
      });
  };


  // Ban account features
  $scope.lockAccount = function (account) {
    $scope.selectedAccount = account;
    $('#lockAccountModal').modal('show');
  };

  $scope.lockAccountWithReason = function (selectedAccount) {
    var lockData = {
      accountId: selectedAccount.id,
      reason: $scope.lockReason
    };

    $http.put('/rest/profile/lock', lockData)
      .then(function (response) {
        toastr.success('Tài khoản đã được khóa thành công.');
        $('#lockAccountModal').modal('hide');
        location.reload();
      })
      .catch(function (error) {
        toastr.error('Khóa tài khoản thất bại.');
      });
  };

  // Function to get lock details and count
  $scope.lockCount = 0;
  $scope.lockDetails = [];
  $scope.selectedAccount = {};

  // Hàm để lấy chi tiết khóa tài khoản
  $scope.getLockDetails = function (accountId) {
    $http.get('/rest/profile/lockDetails/' + accountId)
      .then(function (response) {
        $scope.lockCount = response.data.lockCount;
        $scope.lockDetails = response.data.lockDetails;
        // Xử lý dữ liệu nhận được từ backend ở đây nếu cần thiết
      })
      .catch(function (error) {
        console.error('Error fetching lock details: ', error);
      });
  };

  $scope.openLockDetailsModal = function (accountId) {
    $scope.getLockDetails(accountId);
    $('#lockDetailsModal').modal('show');
  };

  $scope.setSelectedAccount = function (account) {
    $scope.selectedAccount = account;
  };

  // Searching for authority.html
  $scope.searchAccounts = function () {
    if ($scope.searchText) {
      $http.get("/rest/authorities/searchAccounts?name=" + $scope.searchText)
        .then(function (response) {
          $scope.listStaff = response.data;
        })
        .catch(function (error) {
          console.error("Error fetching accounts:", error);
        });
    } else {
      $scope.initialize();
    }
  };

  // Filter by role
  $scope.filterByRole = function (selectedRole) {
    if (selectedRole && selectedRole.id) {
      $http.get(`/rest/authorities/filterByRole?roleID=${selectedRole.id}`)
        .then(function (response) {
          $scope.listStaff = response.data;
        })
        .catch(function (error) {
          console.error("Error fetching users by role:", error);
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