import React, { Component } from 'react';
import HeaderShell from 'components/Header/HeaderShell';
import CenteredPanel from 'components/CenteredPanel/CenteredPanel';
import Form from 'components/Form/Form';
import FormRow from 'components/Form/FormRow';

class Landing extends Component {
	state = {
		q: '',
	};

	onChange = e => {
		this.setState({
			[e.target.id]: e.target.value,
		});
	};

	render() {
		const { q } = this.state;

		return (
			<HeaderShell>
				<CenteredPanel>
					<Form title="Warren">
						<FormRow>
							<input
								id="q"
								type="text"
								value={q}
								onChange={this.onChange}
								autocomplete="off"
							/>
						</FormRow>
					</Form>
				</CenteredPanel>
			</HeaderShell>
		);
	}
}

export default Landing;
