git add .
x=$(<.revisionCount)
let x=x+1
echo "update from Lexseal No. $x"
git commit -m "update from Lexseal No. $x"
echo $x >.revisionCount
git push -u origin master
