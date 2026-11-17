export function apiErrorMessage(error) {
  const status = error.response?.status;

  if (status === 401) {
    return 'Your session is no longer valid. Sign in again to continue.';
  }
  if (status === 403) {
    return 'You do not have permission to access this information.';
  }
  if (error.name === 'Error' && error.message) {
    return error.message;
  }
  if (!error.response) {
    return 'Could not connect to the server. Check that the backend is running at the configured API URL.';
  }

  return error.response.data?.message
    || error.response.data?.detail
    || `The request failed with status ${status}.`;
}
