var userId = "";


/** loads the user's current room from the server
 * callback is called when the request is successful */
function getUserDetails(callback,ctx)
{
  $.ajax({url:"userDetails",dataType:"json",cache: false,data:{userId:userId},
    method:"post",
    success: function(data) {callback(data,ctx)},
    error: function(xhr, status, err)
    {
      console.error("getUserDetails", status, err.toString());
    }
  });
}


/** start waiting for SSEs
 * callback is called whenever there is a new event
 * url is the url that the SSEs are coming from */
function openEventSource(callback,url,room)
{
  var source = new EventSource(url+"?roomId="+room);
  source.onmessage = function(e)
  {
    callback(e.data);
    this.close();
    openEventSource(callback,url,room);
  };
  source.onerror = function(e) {console.error("couldn't get event stream")};
}


/******************************************<Description />
 * contains data about the game world to the user */
var Description = React.createClass({

  /** how the tag starts off */
  getInitialState: function()
  {
    return {content:"nice one",exits:[],users:[],command:"",error:false,
            errorMsg:"",chat:""};
  },

  /** occurs when the tag is mounted */
  componentDidMount: function()
  {
    getUserDetails(function(user,ctx)
    {
        ctx.loadRoomData(user);
        openEventSource(ctx.handleEvent,"events",user.roomId);
    },this);
  },

  /** load a room into the description based on a user's details */
  loadRoomData: function(user)
  {
    $.ajax({url:"roomDetails",dataType:"json",cache: false,data:{roomId:user.roomId},
      method:"post",
      success: function(data)
      {
        $("chat").html("");
        this.setState({content:data.description,exits:data.exits,users:data.users});
      }.bind(this),
      error: function(xhr, status, err)
      {
        console.error(this.props.url, status, err.toString());
      }.bind(this)
    });
  },

  /** handles an event sent from the server */
  handleEvent: function(event)
  {
    var type = event.substring(0,event.indexOf(" "));
    var args = event.substring(event.indexOf(" ") + 1);


    if (type == "newUser") this.setState({users:this.state.users.concat(args)})

    if (type == "leave")
    {
      var newUsers = this.state.users;
      for (var i = this.state.users.length;i >= 0;i--)
      {
        if (this.state.users[i] == args)
        {
          this.setState({users:this.state.users.splice(i-1,1)});
        }
      }
    }

    if (type == "newPath")
    {
      console.log(args);
      this.setState({exits:this.state.exits.concat(args)});
    }


    if (type == "say")
    {
      var chat = $("#chat");
      chat.html(chat.html()+args+"<br />");
    }
  },

  /** when the text in the form is changed */
  handleTextChange: function(e)
  {
    this.setState({command: e.target.value,error:false});
  },

  /** when the submit button is pressed */
  handleSubmit: function(e)
  {
    e.preventDefault();
    var text = this.state.command.trim();
    if (!text) return;
    this.setState({text: ''});

    $.ajax({
      url:"execute",dataType:"text",cache: false,data:{userId:userId,command:this.state.command},method:"post",
      success: function(data)
      {
        getUserDetails(this.loadRoomData);
      }.bind(this),

      error: function(xhr, status, err)
      {
        this.setState({error:true,errorMsg:"couldn't do command \""
                                           +this.state.command+"\""});
      }.bind(this)
    });
  },

  /** renders the tag */
  render: function()
  {
    var exits = [];
    for (var i = 0; i < this.state.exits.length;i++)
    {
      exits.push(<li key={i}>"path{i}" leads to {this.state.exits[i]}</li>);
    }
    var users = [];
    for (var i = 0;i < this.state.users.length;i++)
    {
      users.push(<li key={i}>{this.state.users[i]}</li>);
    }

    return (
      <div className="description">
        <h1>You are at {this.state.content}</h1>

        <ul>{exits}</ul>

        <h2>Users in this area are:</h2>
        <ul>{users}</ul>

        <form className="commandForm" onSubmit={this.handleSubmit}>
          <input type="text" placeholder="what do you do?"
          value={this.state.command} onChange={this.handleTextChange} />
        </form>

        <p className="error" hidden={!this.state.error}>{this.state.errorMsg}</p>

        <div id="chat"></div>
      </div>
    );
  }
});



userId = $("#userId").html();



ReactDOM.render(
  <Description />,
  document.getElementById('content')
);
