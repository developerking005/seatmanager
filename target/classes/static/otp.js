function verifyOtp() {

  const otp = document.getElementById("otp").value;

  fetch("/api/auth/verify-otp", {
    method: "POST",
    headers: { "Content-Type": "application/json" },
    body: JSON.stringify({ otp })
  })
  .then(res => {
    if (!res.ok) throw new Error("Invalid OTP");
    return res.text();
  })
  .then(() => {
    window.location.href = "/dashboard.html";
  })
  .catch(err => alert(err.message));
}
