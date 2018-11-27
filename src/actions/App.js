export function errorMessage(message, error) {
	return dispatch => {
		return dispatch(appError(message, error));
	};
}

export function infoMessage(message) {
	return dispatch => {
		return dispatch(appInfo(message));
	};
}

export function removeMessage(i) {
	return dispatch => {
		return dispatch({
			type: 'APP_REMOVE_MESSAGE',
			index: i,
		});
	};
}

// Helper functions so other actions can dispatch these message
export function appError(message, error) {
	return {
		type: 'APP_SHOW_ERROR',
		createdAt: Date.now(),
		message,
		error,
	};
}

export function appInfo(message) {
	return {
		type: 'APP_SHOW_INFO',
		createdAt: Date.now(),
		message,
	};
}
