const app = (
	state = {
		messages: [],
	},
	action
) => {
	switch (action.type) {
		case 'APP_SHOW_INFO': {
			const message = {
				message: action.message,
				createdAt: action.createdAt,
				type: 'info',
			};
			return {
				...state,
				messages: [message, ...state.messages],
			};
		}
		case 'APP_SHOW_ERROR': {
			console.error(action.error);
			const message = {
				message: action.message,
				createdAt: action.createdAt,
				type: 'error',
				error: action.error,
			};
			return {
				...state,
				messages: [message, ...state.messages],
			};
		}
		case 'APP_REMOVE_MESSAGE': {
			return {
				...state,
				messages: [
					...state.messages.slice(0, action.index),
					...state.messages.slice(action.index + 1),
				],
			};
		}
		default:
			return state;
	}
};

export default app;
