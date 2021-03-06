import React, {Component} from 'react';
import {connect} from 'react-redux';
import {createRoom, getSession} from './../../store/actions/actions';
import {clearSession} from './../../store/actions/storeActions';
import FAQ from './../home/FAQ';

class Home extends Component {

  state = {
    showHashInput: false,
    showLevelInput: false,
  };

  constructor(props) {
    super(props);

    this.scenarioLvl = React.createRef();
    this.scenarioNum = React.createRef();
    this.roomCode = React.createRef();

    this.setShow = this.setShow.bind(this);
    this.createRoom = this.createRoom.bind(this);
    this.joinRoom = this.joinRoom.bind(this);
    this.handleKeydown = this.handleKeydown.bind(this);
  }

  renderCreateRoomBtn(text) {
    return (
      <button className="button is-dark is-large themed-font m-2" id="showLevelInput" type="button"
              onClick={(this.state.showLevelInput ? this.createRoom : this.setShow)}>
        {text}
      </button>
    );
  }

  renderJoinRoomBtn(text) {
    return (
      <button className="button is-dark is-large themed-font m-2" id="showHashInput" type="button"
              onClick={(this.state.showHashInput ? this.joinRoom : this.setShow)}>
        {text}
      </button>
    );
  }

  setShow(event) {
    if (event.target.id === 'showLevelInput') {
      this.setState({'showHashInput': false});
    } else if (event.target.id === 'showHashInput') {
      this.setState({'showLevelInput': false});
    }

    this.setState({[event.target.id]: true});
  }

  renderHashInput() {
    return (
      <>
        <label className="label">Room Code</label>
        <input type="text" className="input is-large" autoFocus name="hash" maxLength={7} ref={this.roomCode} />
        {this.renderJoinRoomBtn("Join")}
      </>
    );
  }

  renderNewRoomInputs() {
    return (
      <>
        <div className="m-1 mr-2">
          <label className="label">Scenario Level</label>
          <div className="select is-large">
            <select className="" name="scenarioLevel" ref={this.scenarioLvl}>
              <option value="0">0</option>
              <option value="1">1</option>
              <option value="2">2</option>
              <option value="3">3</option>
              <option value="4">4</option>
              <option value="5">5</option>
              <option value="6">6</option>
              <option value="7">7</option>
            </select>
          </div>
        </div>
        {this.renderCreateRoomBtn("Enter")}
      </>
    );
  }

  joinRoom() {
    this.props.clearSession();
    this.props.history.push(`/rooms/${this.roomCode.current.value}`);
    this.setState({showHashInput: false});
  }

  createRoom() {
    const newRoom = {scenarioNumber: 0, scenarioLevel: this.scenarioLvl.current.value};
    this.props.createRoom(newRoom);
    this.setState({showLevelInput: false});
  }

  componentDidMount() {
    document.addEventListener("keydown", this.handleKeydown);
  }

  componentWillUnmount() {
    document.removeEventListener("keydown", this.handleKeydown);
  }

  handleKeydown = (event) => {
    if (event.keyCode === 13) {
      if (this.state.showLevelInput) {
        this.createRoom();
      } else if (this.state.showHashInput) {
        this.joinRoom();
      }
    }
  };

  render() {
    return (
      <div className="container">
        <div className="card full-height">
          <div className="card-content">
            <h1 className="title themed-font level-item">Gloomtility</h1>

            <p className="level">
              Gloomtility is a tool for Gloomhaven Players who want assistance managing
              the game, Gloomtility is a web-based board game assistance tool that speeds
              up the pace of play, while making it easier to manage all content. Unlike the base
              game out of the box, our project reduces the number of physical tokens and trackers
              players need to manage.
            </p>


            <div className="columns is-mobile is-centered">
              <div className="column is-narrow"> {this.renderCreateRoomBtn("Create Room")}</div>
              <div className="column is-narrow">{this.renderJoinRoomBtn("Join Room")}</div>
            </div>
            <div className="columns is-centered">
              <div className="column is-narrow"> {this.state.showLevelInput ? this.renderNewRoomInputs() : ''}</div>
              <div className="column is-narrow">{this.state.showHashInput ? this.renderHashInput() : ''}</div>
            </div>
          </div>
          {/*<span className="level-item">*/}
          {/*{this.state.showLevelInput || this.state.showHashInput ? this.renderOptionInputs() : this.renderDefaultButtons()}*/}
          {/*</span>*/}

          <hr className='break'/>

            <FAQ/>

        </div>
      </div>
    );
  }
}


const mapStateToProps = (state) => {
  return {
    ...state
  };
};

const mapDispatchToProps = (dispatch, ownProps) => {
  const rerouteToRoomPage = (response) => {
    ownProps.history.push(`/rooms/${response.data.hash}/`);
  };

  return {
    createRoom: (room) => dispatch(createRoom(rerouteToRoomPage, room)),
    getSession: (hash) => dispatch(getSession(hash)),
    clearSession: () => dispatch(clearSession)
  }
};


export default connect(mapStateToProps, mapDispatchToProps)(Home);