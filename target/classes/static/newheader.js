function goToDashboard() {
  window.location.href = "/dashboard.html";
}

function toggleProfileMenu() {
  const menu = document.getElementById("profileMenu");
  menu.style.display = menu.style.display === "block" ? "none" : "block";
}

function logout() {
  fetch("/logout", { method: "POST" })
    .then(() => window.location.href = "/login.html");
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