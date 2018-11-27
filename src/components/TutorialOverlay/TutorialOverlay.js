import React, { Component } from 'react';
import { createPortal } from 'react-dom';

import styles from './TutorialOverlay.module.css';

class TutorialOverlay extends Component {
	state = {
		progress: 0,
	};

	progressSlides = () => {
		if (this.state.progress + 1 < this.props.slides.length) {
			this.setState({ progress: this.state.progress + 1 });
		} else {
			this.props.onFinished();
		}
	};

	render() {
		const { progress } = this.state;
		const slide = this.props.slides[progress];

		return createPortal(
			<div className={styles.root} onClick={this.progressSlides}>
				<div className={styles.textContainer}>{slide.html}</div>
				<div className={styles.continueNotice}>
					Click anywhere to continue...
				</div>
			</div>,
			document.querySelector('#overlays')
		);
	}
}

export default TutorialOverlay;
