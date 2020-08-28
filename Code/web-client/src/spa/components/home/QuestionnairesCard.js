// react
import React from 'react'
// material-ui components
import Card from '@material-ui/core/Card'
import CardActionArea from '@material-ui/core/CardActionArea'
import CardMedia from '@material-ui/core/CardMedia'
import CardContent from '@material-ui/core/CardContent'
import Typography from '@material-ui/core/Typography'
import { makeStyles } from '@material-ui/core/styles'

import iconBlack from '../../images/icons/services/blk/questionnaireBlk.png'
import iconYellow from '../../images/icons/services/color/questionnaireYlw.png'

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
}));

export default function QuestionnairesCard(props) {

  const classes = useStyles();
  const [onOver, setOnMouseOver] = React.useState(0);
  const [icon, setIcon] = React.useState(iconBlack);

  const handleOnMouseOver = () => {
    setOnMouseOver(10);
    setIcon(iconYellow);
  };
  const handleOnMouseOut = () => {
    setOnMouseOver(0);
    setIcon(iconBlack);
  };

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
          <Typography paragraph component="h1">Questionnaires</Typography>
          <Typography className={classes.text} variant="body2" color="textSecondary" component="p">
            To busy to prepare homework for your students that always fall asleep in class?
            Or coding problems to find the new coding star of your company?
            Then create a test with pre-existent challenges and put them to the test!
          </Typography>
        </CardContent>
      </Card>
    </CardActionArea>
  );
}
