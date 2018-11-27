import React, { Component } from 'react';
import { Route, Switch, Redirect } from 'react-router-dom';
import { connect } from 'react-redux';

import { createPortal } from 'react-dom';
import { bindActionCreators } from 'redux';
import { removeMessage } from 'actions/App';

import { MdClose } from 'react-icons/md';

import history from 'history.js';

// Views
import Landing from 'views/Landing/Landing';

import styles from './App.module.css';

const AppMessage = ({ message, type, index, onClose }) => {
	return (
		<div key={index} className={styles.message} data-type={type}>
			<MdClose onClick={() => onClose(index)} />
			<span>{message}</span>
		</div>
	);
};

class App extends Component {
	previousLocation = this.props.location;
	onClose = undefined;

	componentDidMount() {
		setInterval(this.cleanMessages, 1000);
	}

	cleanMessages = () => {
		this.props.app.messages.forEach((msg, i) => {
			if (msg.createdAt + 5000 < Date.now()) {
				this.props.removeMessage(i);
			}
		});
	};

	componentWillUpdate(nextProps) {
		let { location } = this.props;
		let nextLocation = nextProps.location;

		if (this.onClose) {
			this.onClose();
		}

		// Where we are going has an onClose
		if (nextLocation.state && nextLocation.state.onClose) {
			this.onClose = nextLocation.state.onClose;
		} else {
			this.onClose = undefined;
		}

		if (
			nextProps.history.action !== 'POP' &&
			(!location.state || !location.state.modal)
		) {
			this.previousLocation = this.props.location;
		}
	}

	renderMessages = messages => {
		return (
			<div className={styles.messageContainer}>
				{messages.map((msg, i) => {
					return (
						<AppMessage
							key={i}
							message={msg.message}
							type={msg.type}
							index={i}
							onClose={i => this.props.removeMessage(i)}
						/>
					);
				})}
			</div>
		);
	};

	renderModals = () => {
		return false;

		//const back = e => {
		//	e.stopPropagation();
		//	history.goBack();
		//};
		//return createPortal(
		//	<div className={styles.modalDimmer} onClick={back}>
		//		<div className={styles.modalPanel} onClick={e => e.stopPropagation()}>
		//			<Route
		//				path="/projects/:project/sheets/:sheet/new-dataset"
		//				component={NewDataset}
		//			/>
		//			<Route
		//				path="/projects/:project/sheets/:sheet/new-column"
		//				component={NewColumn}
		//			/>
		//		</div>
		//	</div>,
		//	document.querySelector('#overlays')
		//);
	};

	renderAuthorized(isModal) {
		return (
			<Switch location={isModal ? this.previousLocation : this.props.location}>
				<Route path="/" exact component={Landing} />
				{!isModal && <Redirect to={{ pathname: '/' }} />}
			</Switch>
		);
	}

	render() {
		const { app, location } = this.props;

		const isModal = !!(
			location.state &&
			location.state.modal &&
			this.previousLocation !== location
		); // Never have a modal open on the initial render, refreshing pages will redirect to root

		return (
			<div className={styles.frame}>
				<div className={styles.app}>
					{this.renderMessages(app.messages)}
					{this.renderAuthorized(isModal)}
					{isModal ? this.renderModals() : null}
				</div>
			</div>
		);
	}
}

const mapStateToProps = state => ({
	app: state.app,
});

const mapDispatchToProps = dispatch => {
	return {
		removeMessage: bindActionCreators(removeMessage, dispatch),
	};
};

export default connect(
	mapStateToProps,
	mapDispatchToProps
)(App);
