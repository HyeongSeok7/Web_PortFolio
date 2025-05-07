// check-login 엔드포인트에 요청을 보내 로그인 여부 확인
fetch('/check-login')
    .then(response => response.text())  // 응답을 텍스트로 받기
    .then(data => {
        const authSection = document.getElementById("authSection");
        const stars = document.querySelectorAll(".star-icon");
        if (data.includes("로그인 상태입니다")) {
            // 로그인 상태이면 '마이페이지'와 '로그아웃' 버튼 표시
            authSection.innerHTML = `
                        <a href="/mypage"><span>마이페이지</span></a>
                        <a href="/logout"><span>로그아웃</span></a>
                    `;
        } else {
            // 비로그인 상태이면 '로그인 / 회원가입' 표시
            authSection.innerHTML = `
                        <a href="login"><span>로그인 / 회원가입</span></a>
                    `;
            // 로그인하지 않은 경우 별 숨기기
            stars.forEach(star => {
                star.style.display = "none";
            });
        }
    })
    .catch(error => {
        console.error("오류 발생:", error);
        document.getElementById("authSection").innerText = "로그인 상태를 확인할 수 없습니다.";
    });


// 즐겨찾기 설정
document.addEventListener('DOMContentLoaded', function () {
    const stars = document.querySelectorAll('.star-icon');

    fetch('/getFavoriteEvents')
        .then(response => response.json())
        .then(favoriteEventIds => {
            stars.forEach(star => {
                const eventId = star.dataset.eventId;
                if (favoriteEventIds.includes(eventId)) {
                    star.classList.add('filled');
                }
            });
        })
        .catch(error => console.error('Error loading favorites:', error));

    stars.forEach(star => {
        star.addEventListener('click', function () {
            const eventId = this.dataset.eventId;
            const isFilled = this.classList.contains('filled');
            const url = isFilled ? '/removeFavoriteEvent' : '/addFavoriteEvent';
            const method = isFilled ? 'DELETE' : 'POST';

            fetch(url, {
                method: method,
                headers: { 'Content-Type': 'application/json' },
                body: JSON.stringify({ event_id: eventId })
            }).then(response => {
                if (response.ok) {
                    this.classList.toggle('filled');
                } else {
                    alert('작업 실패');
                }
            }).catch(error => {
                console.error('Error:', error);
                alert('서버 오류');
            });
        });
    });
});

// 정렬 설정 (최신개최순, 마감임박순)
document.addEventListener('DOMContentLoaded', function () {
    AOS.init(); // AOS 애니메이션 초기화

    const sortSelect = document.getElementById('sortOption');
    if (sortSelect) {
        sortSelect.addEventListener('change', sortEvents);
    }

    sortEvents(); // 페이지 로딩 시 초기 정렬 적용
});

function parseDate(str) {
    if (!str) return new Date('2100-01-01');
    if (str.includes('-')) return new Date(str); // ISO 형식
    if (str.length === 8) {
        const year = str.substring(0, 4);
        const month = str.substring(4, 6);
        const day = str.substring(6, 8);
        return new Date(`${year}-${month}-${day}`);
    }
    return new Date('2100-01-01');
}

function sortEvents() {
    const option = document.getElementById('sortOption').value;
    const list = document.getElementById('eventList');
    const cards = Array.from(list.querySelectorAll('.festival-card'));

    cards.sort((a, b) => {
        const startA = parseDate(a.getAttribute('data-start'));
        const startB = parseDate(b.getAttribute('data-start'));
        const endA = parseDate(a.getAttribute('data-end'));
        const endB = parseDate(b.getAttribute('data-end'));

        if (option === 'latest') {
            // 시작일 기준 최신순 (늦게 시작하는 게 먼저)
            return startB - startA;
        } else {
            // 종료일 기준 마감임박순 (빨리 끝나는 게 먼저)
            return endA - endB;
        }
    });

    cards.forEach(card => list.appendChild(card));
    AOS.refresh();
}

document.addEventListener('DOMContentLoaded', function () {
    AOS.init();

    const sortSelect = document.getElementById('sortOption');
    if (sortSelect) {
        sortSelect.addEventListener('change', sortEvents);
    }

    sortEvents(); // 초기 정렬 적용
});