var Description = React.createClass({
  render: function() {
    return (
      <div className="commentBox">
        {this.props.content}
      </div>
    );
  }
});


ReactDOM.render(
  <CommentBox content="a nice creek with stuff and that"/>,
  document.getElementById('content')
);
