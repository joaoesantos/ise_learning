// react
import React from 'react'
// material-ui components
import Card from '@material-ui/core/Card'
import CardActionArea from '@material-ui/core/CardActionArea'
import CardMedia from '@material-ui/core/CardMedia'
import CardContent from '@material-ui/core/CardContent'
import { makeStyles } from '@material-ui/core/styles'
import Typography from '@material-ui/core/Typography'
// icons
import iconBlack from '../../images/icons/services/blk/challengeBlk.png'
import iconYellow from '../../images/icons/services/color/challengeYlw.png'

const useStyles = makeStyles((theme) => ({
  layout: {
    width: '400px',
    height: '250px',
    textAlign: 'center',
    textDecoration: 'none',
    display: 'flex',
    flexDirection: 'column',
    alignItems: 'center',
    justifyContent: 'center',
  },
  media: {
    width: 50,
    height: 50,
  },
}))

export default function ChallengesCard(props) {

  const classes = useStyles()
  const [onOver, setOnMouseOver] = React.useState(0)
  const [icon, setIcon] = React.useState(iconBlack)

  const handleOnMouseOver = () => {
    setOnMouseOver(10);
    setIcon(iconYellow);
  }

  const handleOnMouseOut = () => {
    setOnMouseOver(0);
    setIcon(iconBlack);
  }

  return (
    <CardActionArea onClick={props.handleOnClick}>
      <Card className={classes.layout}
        elevation={onOver}
        onMouseOver={handleOnMouseOver}
        onMouseOut={handleOnMouseOut}
      >
        <CardMedia
          className={classes.media}
          image={icon}
        />
        <CardContent>
          <Typography paragraph component="h1">Challenges</Typography>
          <Typography className={classes.text} variant="body2" color="textSecondary" component="p">
            Coding interview preparation is a numbers game that many cadidates lose. 
            So why don't you share your expertise, make a problem that has kicked yout butt and 
            help other fellow coders not to do the same mistakes?
          </Typography>
        </CardContent>
      </Card>
    </CardActionArea>
  )
}
