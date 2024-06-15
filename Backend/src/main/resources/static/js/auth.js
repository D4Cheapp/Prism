async function confirm(apiUrl) {
  const backendUrl = document.getElementById('backendUrl').value;
  const urlParams = new URLSearchParams(window.location.search);
  const code = urlParams.get('code');
  const data = await fetch(`${backendUrl ? backendUrl : 'http://localhost:8080'}/prism/v1/auth${apiUrl + code}`, {
    method: 'POST',
  })
    .then(data => data.json());
  if (data.error) {
    showInfo(data.error, false);
  }
  if (data.info) {
    showInfo(data.info, true);
  }
}

async function resetPassword() {
  const backendUrl = document.getElementById('backendUrl').value;
  const password = document.getElementById('password').value;
  const confirmPassword = document.getElementById('confirmPassword').value;
  const urlParams = new URLSearchParams(window.location.search);
  const code = urlParams.get('code');
  const data = await fetch(`${backendUrl ? backendUrl : 'http://localhost:8080'}/prism/v1/auth/user/restore-password/confirm/${code}`, {
    method: 'PATCH',
    headers: {
      'Content-Type': 'application/json'
    },
    body: {
      password,
      confirmPassword
    }
  })
    .then(data => data.json());
  if (data.error) {
    showInfo(data.error, false);
  }
  if (data.info) {
    showInfo(data.info, true);
  }
}

function showInfo(message, success) {
  const infoContainer = document.getElementById('infoContainer');
  const infoMessage = document.getElementById('infoMessage');
  infoContainer.classList.add(success ? 'success' : 'error');
  infoMessage.textContent = message;
  infoContainer.style.display = 'flex';
  const timeout = setTimeout(() => {
    infoContainer.style.display = 'none';
    infoMessage.textContent = '';
    infoContainer.classList.remove(success ? 'success' : 'error');
  }, 5000);
  return () => clearTimeout(timeout);
}

function showCustomBackendUrl() {
  const backendUrlContainer = document.getElementById('backendUrlContainer');
  const isContainerVisible = backendUrlContainer.classList.contains('showContainer');
  backendUrlContainer.classList = isContainerVisible ? 'inputContainer hideContainer' : 'inputContainer showContainer';
}
