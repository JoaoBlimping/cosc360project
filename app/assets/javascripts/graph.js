const width = 500;
const height = 500;


/** downloads the event data from the server */
function getEvents(callback,ctx)
{
  $.ajax({url:"pastEvents",dataType:"json",cache: false,method:"get",
    success: function(data) {callback(data,ctx)},
    error: function(xhr, status, err)
    {
      console.error("previousEvents",status,err.toString());
    }
  });
}


function distance(x1,y1,x2,y2)
{
  return Math.sqrt(Math.pow(x1 - x2,2) + Math.pow(y1 - y2,2));
}


var nodes = [];


var Display = React.createClass({
  /** occurs when the tag is mounted */
  componentDidMount: function()
  {
    setInterval(this.force,100);

    //make the thing start getting the data, when it's done call populate,
    getEvents(function(data,context)
    {
      var events = data.events;
      for (var i = 0;i < events.length;i++)
      {
        if (!nodes.hasOwnProperty(events[i].roomId)) nodes[events[i].roomId] =
            {"x":Math.random() * width,"y":Math.random() * height,"nEvents":1,
             "vx":0,"vy":0,"exits":events[i].exits};
        else nodes[events[i].roomId].nEvents++;
      }
    },this);
  },

  /** performs a physics step on the nodes, and redisplays them */
  force: function()
  {
    //calculate the force on the nodes
    for (var x in nodes)
    {
      for (var y in nodes)
      {
        if (x == y) continue;

        nodes[y].vx += Math.sin(nodes[y].x - nodes[x].x);
        nodes[y].vy += Math.sin(nodes[y].y - nodes[x].y);
      }
    }

    //apply the force
    for (var x in nodes)
    {
      nodes[x].x += nodes[x].vx;
      nodes[x].y += nodes[x].vy;
      nodes[x].vx = 0;
      nodes[x].vy = 0;
    }

    //display the nodes
    var ctx = $("#display").get(0).getContext("2d");
    ctx.fillStyle = "lightgreen";
    ctx.fillRect(0,0,width,height);

    for (var i in nodes)
    {
      ctx.beginPath();
      ctx.arc(nodes[i].x,nodes[i].y,nodes[i].nEvents * 2,0,2*Math.PI);
      ctx.fillStyle = "black";
      ctx.stroke();

      for (var line in nodes[i].exits)
      {
        if (!nodes.hasOwnProperty(line))
        {
          continue;
        }

        ctx.moveTo(nodes[i].x,nodes[i].y);
        ctx.lineTo(nodes[line].x,nodes[line].y);
        ctx.stroke();
      }
    }


  },


  /** renders the tag */
  render: function()
  {
    return (
      <canvas id="display" className="display" width="500px" height="500px"></canvas>
    );
  }
});



ReactDOM.render(
  <Display />,
  document.getElementById('content')
);
