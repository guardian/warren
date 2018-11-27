import React, { Component } from 'react';
import PopOver from 'components/PopOver/PopOver';

import Button from 'components/Button/Button';

const defaultProxy = ({ isOpen }) => {
	return <Button>{isOpen ? 'close' : 'open'}</Button>;
};

class WithPopOver extends Component {
	constructor(props) {
		super(props);
		this.proxyRef = React.createRef();
	}

	state = {
		isOpen: false,
		openAt: [0, 0],
	};

	setOpen(isOpen) {
		const clientRect = this.proxyRef.current.getBoundingClientRect();
		this.setState({
			isOpen,
			openAt: [
				clientRect.x + clientRect.width,
				clientRect.y + clientRect.height,
			],
		});
	}

	render() {
		const { isOpen, openAt } = this.state;
		const { children } = this.props;
		const Proxy = this.props.proxy ? this.props.proxy : defaultProxy;
		return (
			<>
				<div
					ref={this.proxyRef}
					onClick={() => {
						this.setOpen(true);
					}}
				>
					<Proxy isOpen={isOpen} />
				</div>
				{isOpen && (
					<PopOver
						left={openAt[0]}
						top={openAt[1]}
						onClose={() => {
							this.setOpen(false);
						}}
					>
						{children}
					</PopOver>
				)}
			</>
		);
	}
}

export default WithPopOver;
