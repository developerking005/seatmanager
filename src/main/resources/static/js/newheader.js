function goToDashboard() {
  window.location.href = "/dashboard.html";
}

function toggleProfileMenu() {
  const menu = document.getElementById("profileMenu");
  menu.style.display = menu.style.display === "block" ? "none" : "block";
}

function logout() {
  fetch("/logout", { method: "POST" })
    .then(() => window.location.href = "/newindex.html");
}

// close dropdown on outside click
document.addEventListener("click", (e) => {
  const menu = document.getElementById("profileMenu");
  if (!e.target.closest(".profile-wrapper")) {
    menu && (menu.style.display = "none");
  }
});



document.addEventListener("DOMContentLoaded", () => {
  const currentPage = window.location.pathname.split("/").pop();

  document.querySelectorAll(".nav-link").forEach(link => {
    const linkPage = link.getAttribute("href").split("/").pop();

    if (linkPage === currentPage) {
      link.classList.add("active");
    } else {
      link.classList.remove("active");
    }
  });
});




document.addEventListener("DOMContentLoaded", () => {
  const currentPath = window.location.pathname;

  document.querySelectorAll(".mobile-footer .nav-item").forEach(item => {
    if (currentPath.endsWith(item.dataset.path)) {
      item.classList.add("active");
    }
  });
});


function toggleMobileMenu() {
  const menu = document.getElementById("mobileMenu");
  menu.classList.toggle("hidden");
}

/* Optional: close menu on outside click */
document.addEventListener("click", function (e) {
  const menu = document.getElementById("mobileMenu");
  const btn = document.querySelector(".mobile-menu-btn");

  if (!menu || menu.classList.contains("hidden")) return;

  if (!menu.contains(e.target) && !btn.contains(e.target)) {
    menu.classList.add("hidden");
  }
});
