document.getElementById("generateBtn").addEventListener("click", async function() {
            this.classList.add("loading");
            try {
                const res = await fetch("/api/inbox/create", { method: "POST" });
                if (!res.ok) throw new Error("Server error");
                const data = await res.json();
                localStorage.setItem("inboxToken", data.accessToken);
                window.location.href = `/inbox.html?email=${encodeURIComponent(data.emailAddress)}`;
            } catch (err) {
                alert("Failed to create inbox. Please try again.");
                this.classList.remove("loading");
            }
        });
function toggleFaq(el) {
    const item = el.parentElement;
    const isOpen = item.classList.contains('open');
    document.querySelectorAll('.faq-item').forEach(i => i.classList.remove('open'));
    if (!isOpen) item.classList.add('open');
}

let selectedRating = 0;
const stars = document.querySelectorAll('.star');
const ratingLabel = document.getElementById('ratingLabel');
const labels = ['', 'Poor', 'Fair', 'Good', 'Great', 'Excellent!'];

stars.forEach(star => {
    star.addEventListener('mouseover', () => {
        const val = +star.dataset.val;
        stars.forEach(s => s.classList.toggle('active', +s.dataset.val <= val));
    });
    star.addEventListener('mouseout', () => {
        stars.forEach(s => s.classList.toggle('active', +s.dataset.val <= selectedRating));
    });
    star.addEventListener('click', () => {
        selectedRating = +star.dataset.val;
        ratingLabel.textContent = labels[selectedRating];
        document.getElementById('feedbackForm').style.display = 'flex';
        stars.forEach(s => s.classList.toggle('active', +s.dataset.val <= selectedRating));
    });
});

document.getElementById('submitFeedback').addEventListener('click', () => {
    document.getElementById('feedbackForm').style.display = 'none';
    document.getElementById('feedbackThanks').style.display = 'flex';
});

function animateCount(el, target, suffix = '') {
    let current = 0;
    const step = Math.ceil(target / 40);
    const timer = setInterval(() => {
        current = Math.min(current + step, target);
        el.textContent = current.toLocaleString() + suffix;
        if (current >= target) clearInterval(timer);
    }, 30);
}

window.addEventListener('load', () => {
    setTimeout(() => {
        animateCount(document.getElementById('statEmails'), 1284);
        animateCount(document.getElementById('statInboxes'), 847);
    }, 400);
});