function toggleProfileMenu() {
  const menu = document.getElementById("profileMenu");
  menu.style.display = menu.style.display === "block" ? "none" : "block";
}

function logout() {
  fetch("/api/auth/logout", { method: "POST" })
    .then(() => window.location.href = "/login.html");
}

// Close dropdown on outside click
document.addEventListener("click", function (e) {
  const wrapper = document.querySelector(".profile-wrapper");
  if (wrapper && !wrapper.contains(e.target)) {
    document.getElementById("profileMenu").style.display = "none";
  }
});

fetch("/api/header")
  .then(res => {
    if (!res.ok) throw new Error("Not authenticated");
    return res.json();
  })
  .then(data => {
    document.getElementById("libraryName").innerText = data.libraryName;
    document.getElementById("adminName").innerText = data.adminName;

    if (data.logoUrl) {
      document.getElementById("libraryLogo").src = data.logoUrl;
    }
  })
  .catch(() => {
    window.location.href = "/login.html";
  });


function goToDashboard() {
    window.location.href = "/dashboard.html";
}