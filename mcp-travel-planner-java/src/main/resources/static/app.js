const recommendationsList = document.getElementById('recommendationsList');
const refreshButton = document.getElementById('refreshButton');
const statusEl = document.getElementById('status');

async function loadRecommendations() {
  statusEl.textContent = 'Loading recommendations...';
  recommendationsList.innerHTML = '';

  try {
    const response = await fetch('/api/recommendations');
    if (!response.ok) {
      throw new Error(`Request failed with status ${response.status}`);
    }

    const data = await response.json();

    if (!data.length) {
      statusEl.textContent = 'No recommendations available right now.';
      return;
    }

    statusEl.textContent = `Showing ${data.length} top destination${data.length === 1 ? '' : 's'}.`;

    const items = data.map((destination) => {
      const item = document.createElement('li');
      item.className = 'item';
      item.innerHTML = `
        <div>
          <strong>${destination.name}</strong>
          <div class="meta">${destination.country}</div>
        </div>
        <span class="badge">★ ${destination.rating.toFixed(1)}</span>
      `;
      return item;
    });

    recommendationsList.replaceChildren(...items);
  } catch (error) {
    statusEl.textContent = 'Unable to load recommendations. Please try again.';
    console.error(error);
  }
}

refreshButton.addEventListener('click', loadRecommendations);
loadRecommendations();
