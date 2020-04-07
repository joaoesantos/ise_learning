// react
import React from 'react';
// material-ui components
import clsx from 'clsx';
import Card from '@material-ui/core/Card';
import CardMedia from '@material-ui/core/CardMedia';
import CardContent from '@material-ui/core/CardContent';
import CardActions from '@material-ui/core/CardActions';
import Collapse from '@material-ui/core/Collapse';
import IconButton from '@material-ui/core/IconButton';
import Typography from '@material-ui/core/Typography';
import ExpandMoreIcon from '@material-ui/icons/ExpandMore';
import { makeStyles } from '@material-ui/core/styles';

const useStyles = makeStyles((theme) => ({
  layout: { },
  media: {
    height: 0,
    paddingTop: '56.25%', // 16:9
  },
  text: {
    textAlign: 'center'
  },
  expand: {
    transform: 'rotate(0deg)',
    marginLeft: 'auto',
    transition: theme.transitions.create('transform', {
      duration: theme.transitions.duration.shortest,
    }),
  },
  expandOpen: {
    transform: 'rotate(180deg)',
  }
}));

export default function QuestionnairesCard() {
  const classes = useStyles();
  const [expanded, setExpanded] = React.useState(false);

  const handleExpandClick = () => {
    setExpanded(!expanded);
  };

  return (
    <Card className={classes.layout}>
      <CardMedia
        className={classes.media}
        image={require('./challengeAccepted.jpg')}
      />
      <CardContent>
        <Typography paragraph component="h1">Questionnaires</Typography>
        <Typography className={classes.text} variant="body2" color="textSecondary" component="p">
          To busy to prepare homework for your students that always fall asleep in class?
          Or coding problems to find the new coding star of your company?
          Then create a test with pre-existent problems and challenge them!
        </Typography>
      </CardContent>
      <CardActions disableSpacing>
        <IconButton
          className={clsx(classes.expand, {
            [classes.expandOpen]: expanded,
          })}
          onClick={handleExpandClick}
          aria-expanded={expanded}
          aria-label="show more"
        >
          <ExpandMoreIcon />
        </IconButton>
      </CardActions>
      <Collapse in={expanded} timeout="auto" unmountOnExit>
        <CardContent>
          <Typography paragraph>Yep, YOU were not prepared!</Typography>
        </CardContent>
      </Collapse>
    </Card>
  );
}
