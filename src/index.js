import React from 'react';
import ReactDOM from 'react-dom';
import { HashRouter, Route } from 'react-router-dom';
import { Provider } from 'react-redux';
import { createStore, applyMiddleware } from 'redux';
import { composeWithDevTools } from 'redux-devtools-extension';
import thunk from 'redux-thunk';
import _ from 'lodash';

import App from 'views/App';
import rootReducer from 'reducers';

import 'styles/index.css';

export const loadState = () => {
	try {
		const serializedState = localStorage.getItem('state');
		if (serializedState === null) {
			return undefined;
		}
		return JSON.parse(serializedState);
	} catch (err) {
		return undefined;
	}
};

export const saveState = state => {
	try {
		const serializedState = JSON.stringify(state);
		localStorage.setItem('state', serializedState);
	} catch {
		console.error('Failed to serialize redux state');
	}
};

const persistedStore = loadState();
export const store = createStore(
	rootReducer,
	persistedStore,
	composeWithDevTools(applyMiddleware(thunk))
);

store.subscribe(
	_.throttle(() => {
		saveState(store.getState());
	}, 1000)
);

ReactDOM.render(
	<Provider store={store}>
		<HashRouter>
			<Route component={App} />
		</HashRouter>
	</Provider>,
	document.getElementById('root')
);
