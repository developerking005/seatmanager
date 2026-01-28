let allAlerts = [];
let showAllAlerts = false;

//fetch("http://localhost:8080/api/dashboard")
//  .then(res => res.json())
//  .then(data => {
//    document.getElementById("totalSeats").innerText = data.totalSeats;
//    document.getElementById("filledSeats").innerText = data.filledSeats;
//    document.getElementById("vacantSeats").innerText = data.vacantSeats;
//    document.getElementById("totalCollection").innerText = "₹" + data.totalCollection;
//  });

fetch("http://localhost:8080/api/dashboards")
  .then(res => res.json())
  .then(data => {
      document.getElementById("totalSeats").innerText = data.totalSeats;
      document.getElementById("filledSeats").innerText = data.filledSeats;
      document.getElementById("vacantSeats").innerText = data.vacantSeats;
      document.getElementById("halfDayCount").innerText = data.halfDayStudents;
  });

fetch("http://localhost:8080/api/seats")
  .then(res => res.json())
  .then(seats => renderSeats(seats));

function renderSeats(seats) {
    const container = document.getElementById("seatContainer");
    container.innerHTML = "";

    const seatsPerRow = 10;
    let row = [];

    seats.forEach((seat, index) => {
        row.push(seat);

        if (row.length === seatsPerRow || index === seats.length - 1) {
            // Zig-zag: reverse alternate rows
            const isReverse = Math.floor(index / seatsPerRow) % 2 !== 0;
            const finalRow = isReverse ? [...row].reverse() : row;

            finalRow.forEach(s => {
                const div = document.createElement("div");
                div.classList.add("seat");
                div.innerText = s.seatNumber;
               if (s.occupied) {
                   div.classList.add("occupied");
                   div.onclick = () => openStudentModal(s.seatNumber);
               } else {
                   div.classList.add("vacant");
                   div.onclick = () => bookSeat(s.seatNumber);
               }

                container.appendChild(div);
            });

            row = [];
        }
    });
}


let selectedSeat = null;

function bookSeat(seatNumber) {
    selectedSeat = seatNumber;
    document.getElementById("seatNo").innerText = seatNumber;
    document.getElementById("bookingModal").style.display = "block";
}



//Confirmbooking flow start here
function confirmBooking() {

    const payload = {
        seatNumber: selectedSeat,
        name: document.getElementById("name").value,
        phone: document.getElementById("phone").value,
        amountPaid: document.getElementById("amount").value,
        studentType: "FULL_DAY"
    };

    fetch("http://localhost:8080/api/book", {
        method: "POST",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify(payload)
    })
    .then(res => {
        if (!res.ok) throw new Error("Booking failed");
        return res.text();
    })
    .then(() => {
        closeModal();
        refreshUI();
    })
    .catch(err => alert(err.message));
}


function closeModal() {
    document.getElementById("bookingModal").style.display = "none";
}

function refreshUI() {
    location.reload();
}




//Vavocate flow start here

let vacateSeatNumber = null;


function openVacateModal(seatNumber) {
    vacateSeatNumber = seatNumber;
    document.getElementById("vacateSeatNo").innerText = seatNumber;
    document.getElementById("studentInfo").innerText =
        "Are you sure you want to vacate this seat?";
    document.getElementById("vacateModal").style.display = "block";
}


function confirmVacate() {

    fetch(`http://localhost:8080/api/vacate/${vacateSeatNumber}`, {
        method: "POST"
    })
    .then(res => {
        if (!res.ok) throw new Error("Vacate failed");
        return res.text();
    })
    .then(() => {
        closeVacateModal();
        refreshUI();
    })
    .catch(err => alert(err.message));
}

function closeVacateModal() {
    document.getElementById("vacateModal").style.display = "none";
}



//open student model

let currentSeatNumber = null;
let currentStudentId = null;

function openStudentModal(seatNumber) {
    currentSeatNumber = seatNumber;

    fetch(`/api/student/seat/${seatNumber}`)
        .then(res => {
            if (!res.ok) throw new Error("Student not found");
            return res.json();
        })
        .then(student => {
            currentStudentId = student.id;

            document.getElementById("detailSeatNo").innerText = seatNumber;
            document.getElementById("detailName").value = student.name;
            document.getElementById("detailPhone").value = student.phone;
            document.getElementById("detailJoinDate").value = student.bookingDate;
            document.getElementById("detailExpireDate").value = student.expiryDate;
            document.getElementById("detailAmount").value = student.amountPaid;

                 // 🔥 NEW
                  loadAvailableSeats(seatNumber);

            document.getElementById("studentModal").style.display = "block";
        })
        .catch(err => alert(err.message));
}

function closeStudentModal() {
    document.getElementById("studentModal").style.display = "none";
}


// Vacate Button (Reuse Existing API)
function vacateSeat() {
    fetch(`/api/vacate/${currentSeatNumber}`, { method: "POST" })
        .then(() => {
            closeStudentModal();
            refreshUI();
        });
}


//update button
function updateStudent() {

    const payload = {
        name: document.getElementById("detailName").value,
        phone: document.getElementById("detailPhone").value,
        amountPaid: document.getElementById("detailAmount").value,
        expireDate: document.getElementById("detailExpireDate").value,
         seatNumber: parseInt(document.getElementById("detailSeatNumber").value)
    };

    fetch(`/api/student/${currentStudentId}`, {
        method: "PUT",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify(payload)
    })
    .then(res => {
        if (!res.ok) throw new Error("Update failed");
        return res.text();
    })
    .then(() => {
        alert("Updated successfully");
        closeStudentModal();
        refreshUI();
    })
    .catch(err => alert(err.message));
}




//when dashboard page load
loadExpiryNotifications();

function loadExpiryNotifications() {
//  fetch("/api/student/expiring-soon")
//    .then(res => res.json())
//    .then(renderExpiryList);
    Promise.all([
        fetch("/api/student/expiring-soon").then(r => r.json()),
        fetch("/api/student/expired").then(r => r.json())
      ]).then(([expiring, expired]) => {
        renderExpiryList([...expired, ...expiring]);
      });
}


//function renderExpiryList(data) {
//  const box = document.getElementById("expiryBody");
//  box.innerHTML = "";
//    data.forEach(s => {
////      const statusClass = getExpiryClass(s.expiryDate);
//        const tr = document.createElement("tr");
//      // 🔥 decide row color
//        const rowClass = getExpiryClass(s.expireDate);
//        if (rowClass) {
//          tr.classList.add(rowClass);
//        }
//     tr.innerHTML = `
//         <td>${s.seatNumber}</td>
//         <td>${s.name}</td>
//         <td>${s.phone}</td>
//         <td>${s.expireDate ? s.expireDate : "-"}</td>
//         <td>₹${s.amountPaid}</td>
//         <td>
//           <button onclick="editStudent(${s.id}">Edit</button>
//           <button onclick="sendReminder('${s.phone}', '${s.name}', ${s.seatNumber}, '${s.expireDate}')">Send</button>
//         </td>
//    `;
//    box.appendChild(tr);
//  });
//}



////new JS for Expire subscription
function renderExpiryList(data) {
  allAlerts = data;

  const box = document.getElementById("expiryBody");
  box.innerHTML = "";

  const alertsToShow = showAllAlerts ? allAlerts : allAlerts.slice(0, 3);

  alertsToShow.forEach(s => {
    const statusClass = getExpiryClass(s.expireDate);

    const div = document.createElement("div");
    div.className = "alert-item";

    div.innerHTML = `
      <div class="alert-top">
        <div class="alert-user">
          <div class="avatar">
            <i class="fa-solid fa-user"></i>
          </div>

          <div class="user-info">
            <div class="name">${s.name}</div>
            <div class="meta">Seat: ${s.seatNumber}</div>
          </div>
        </div>

        <span class="status ${statusClass}">
          ${statusClass === "expired" ? "EXPIRED" : "EXPIRING SOON"}
        </span>
      </div>

      <div class="alert-actions">
        <button class="btn whatsapp"
          onclick="sendReminder('${s.phone}', '${s.name}', ${s.seatNumber}, '${s.expiryDate}')">
          <i class="fa-brands fa-whatsapp"></i> WhatsApp
        </button>

        <button class="btn call">
          <i class="fa-solid fa-phone"></i> Call
        </button>
      </div>
    `;

    box.appendChild(div);
  });
   updateViewAllText();
}





function toggleViewAll() {
  showAllAlerts = !showAllAlerts;
  renderExpiryList(allAlerts);
}

function updateViewAllText() {
  const btn = document.querySelector(".view-all");
  btn.innerText = showAllAlerts ? "Show Less" : "View All Alerts";
}



function sendReminder(phone, name, seat, expiry) {
  const msg = `
Hello ${name} 👋

Your library seat (Seat No: ${seat})
is expiring on ${expiry}.

Please renew to continue your seat.
Thank you 🙏
`;

  window.open(
    `https://wa.me/91${phone}?text=${encodeURIComponent(msg)}`,
    "_blank"
  );
}


//date calculation logic / check weather we need it or not
function daysBetween(today, expiry) {
  const oneDay = 24 * 60 * 60 * 1000;
  return Math.floor((expiry - today) / oneDay);
}

function getExpiryClass(expiryDateStr) {
 if (!expiryDateStr) return null;

  const today = new Date();
  today.setHours(0, 0, 0, 0);

  const [year, month, day] = expiryDateStr.split("-").map(Number);
  const expiry = new Date(year, month - 1, day);
  expiry.setHours(0, 0, 0, 0);

  const diffDays = Math.floor(
    (expiry - today) / (1000 * 60 * 60 * 24)
  );

  console.log(diffDays)

  if (diffDays < 0) {
    return "expired";           // 🔴 already expired
  } else if (diffDays === 0) {
    return "expired";           // 🔴 expires today
  } else if (diffDays <= 3) {
    return "expiring-soon";     // 🟠 1–3 days left
  }

  return null;
}


function loadAvailableSeats(currentSeat) {
  fetch("/api/seats")
    .then(res => res.json())
    .then(seats => {
      const select = document.getElementById("detailSeatNumber");
      select.innerHTML = "";

      seats.forEach(seat => {
        // show vacant seats + current seat
        if (!seat.occupied || seat.seatNumber === currentSeat) {
          const option = document.createElement("option");
          option.value = seat.seatNumber;
          option.text = seat.seatNumber;
          if (seat.seatNumber === currentSeat) {
            option.selected = true;
          }
          select.appendChild(option);
        }
      });
    });
}



//forcing to close the student object again and again
window.onload = () => {
    document.getElementById("bookingModal").style.display = "none";
    document.getElementById("studentModal").style.display = "none";
};





//new card click functions
function scrollToSeats() {
  document.querySelector(".seat-card")
    .scrollIntoView({ behavior: "smooth" });
}

function openActiveFullDayStudents() {
  window.location.href = "/students.html";
}

function openHalfDayForm() {
  window.location.href = "/halfday-student.html";
}


// ------------------------------------Now this is for Mobile JS -----------------------
function goTo(path) {
  window.location.href = path;
}