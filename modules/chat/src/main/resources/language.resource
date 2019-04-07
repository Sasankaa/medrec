# at the very beginning
initial: How do you do. Please tell me your problem.
# finally
bye: Goodbye. It was nice talking to you.
# user input means bye.
quit: bye
quit: goodbye
quit: done
quit: exit
quit: quit

# same meanings need replaced for unifying before decomposed.
pre: dont don't
pre: cant can't
pre: wont won't
pre: recollect remember
pre: recall remember
pre: dreamt dreamed
pre: dreams dream
pre: maybe perhaps
pre: might perhaps
pre: certainly yes
pre: machine computer
pre: machines computer
pre: computers computer
pre: were was
pre: you're you are
pre: i'm i am
pre: same alike
pre: identical alike
pre: equivalent alike

# if quoting part of input, some words need converted before reply
post: am are
post: your my
post: me you
post: myself yourself
post: yourself myself
post: i you
post: you I
post: my your
post: i'm you are

# synon list handled before matching regex
synon: belief feel think believe wish
synon: family mother mom father dad sister brother wife children child
synon: desire want need
synon: sad unhappy depressed sick
synon: happy elated glad better
synon: cannot can't
synon: everyone everybody nobody none
synon: be am is are was

# key decomposition structures
# key: word rank
# ===================== rank 0 ==========================
# no appropriate reply
key: noapptrep 0
  decomp: (.*)
    reply: Why do you say that?
    reply: Go on, please.
    reply: You said (0)?
    reply: Can you elaborate on that?
    reply: Are you saying that (0)?
    reply: Does talking about this bother you?
    reply: Do you say (0) for some special reason?
    reply: I'm not sure I understand you fully.
    reply: What does that suggest to you?
    reply: Do you feel strongly about discussing such things?
	reply: That is interesting. Please continue.
	reply: Tell me more about that.

# say hello
key: hello 0
  decomp: (.*)
    reply: Hello, how are you?.
    reply: How do you do. Please state your problem.
    reply: Hi. What seems to be your problem?
    reply: Hi. is everything OK?
    reply: Hello, did everything work for you?
key: fine 0
  decomp: (.*)
    reply: I'd glad to hear that.

# apology
key: sorry 0
  decomp: (.*)
    reply: Please don't apologise.
    reply: Apologies are not necessary.
    reply: I've told you that apologies are not required.
	reply: It did not bother me. Please continue.
key: apologise 0
  decomp: (.*)
    reply: goto sorry

# confidence
key: perhaps 0
  decomp: (.*)
    reply: You don't seem quite certain.
    reply: Why the uncertain tone?
    reply: Can't you be more positive?
    reply: You aren't sure?
    reply: Don't you know?
	reply: How likely, would you estimate?
key: can 0
  decomp: (.*)can you (.*)
    reply: You believe I can (1) don't you?
    reply: goto what
    reply: You want me to be able to (1).
    reply: Perhaps you would like to be able to (1) yourself.
  decomp: (.*)can i (.*)
    reply: Whether or not you can (1) depends on you more than on me.
    reply: Do you want to be able to (1)?
    reply: Perhaps you don't want to (1).
    reply: goto what

# yes or no
key: yes 0
  decomp: (.*)
    reply: You seem to be quite positive.
    reply: You are sure.
    reply: I see.
    reply: I understand.
key: no 0
   decomp: (.*)no one (.*)
	reply: Are you sure, no one (1)?
	reply: Surely someone (1) .
	reply: Can you think of anyone at all?
	reply: Are you thinking of a very special person?
	reply: Who, may I ask?
	reply: You have a particular person in mind, don't you?
	reply: Who do you think you are talking about?
  decomp: (.*)
    reply: Are you saying no just to be negative?
    reply: You are being a bit negative.
    reply: Why not?
    reply: Why 'no'?

# person
key: i 0
  decomp: (.*)i @desire (.*)
    reply: What would it mean to you if you got (1)?
    reply: Why do you want (1)?
    reply: Suppose you got (1) soon.
    reply: What if you never got (1)?
    reply: What would getting (1) mean to you?
    reply: What does wanting (1) have to do with this discussion?
  decomp: (.*)i am(.*)@sad (.*)
    reply: I am sorry to hear that you are @0.
    reply: Do you think coming here will help you not to be @0?
    reply: I'm sure it's not pleasant to be @0.
    reply: Can you explain what made you @0?
  decomp: (.*)i am(.*)@happy (.*)
    reply: How have I helped you to be @0?
    reply: Has your treatment made you @0?
    reply: What makes you @0 just now?
    reply: Can you explain why you are suddenly @0?
  decomp: (.*)i was (.*)
    reply: goto was
  decomp: (.*)i @belief i (.*)
    reply: Do you really think so?
    reply: But you are not sure you @0.
    reply: Do you really doubt you @0?
  decomp: (.*)i(.*)@belief (.*)you (.*)
    reply: goto you
  decomp: (.*)i am (.*)
    reply: Is it because you are (1) that you came to me?
    reply: How long have you been (1)?
    reply: Do you believe it is normal to be (1)?
    reply: Do you enjoy being (1)?
	reply: Do you know anyone else who is (1)?
  decomp: (.*)i @cannot (.*)
    reply: How do you know that you can't (1)?
    reply: Have you tried?
    reply: Perhaps you could (1) now.
    reply: Do you really want to be able to (1)?
	reply: What if you could (1)?
  decomp: (.*)i don't (.*)
    reply: Don't you really (1)?
    reply: Why don't you (1)?
    reply: Do you wish to be able to (1)?
    reply: Does that trouble you?
  decomp: (.*)i feel (.*)
    reply: Tell me more about such feelings.
    reply: Do you often feel (1)?
    reply: Do you enjoy feeling (1)?
    reply: Of what does feeling (1) remind you?
  decomp: (.*)i (.*)you (.*)
    reply: Perhaps in your fantasies we (1) each other.
    reply: Do you wish to (1) me?
    reply: You seem to need to (1) me.
    reply: Do you (1) anyone else?
key: you 0
  decomp: (.*)you remind me of (.*)
    reply: goto alike
  decomp: (.*)you are (.*)
    reply: What makes you think I am (1)?
    reply: Does it please you to believe I am (1)?
    reply: Do you sometimes wish you were (1)?
    reply: Perhaps you would like to be (1).
  decomp: (.*)you(.*)me (.*)
    reply: Why do you think I (1) you?
    reply: You like to think I (1) you -- don't you?
    reply: What makes you think I (1) you?
    reply: Really, I (1) you?
    reply: Do you wish to believe I (1) you?
    reply: Suppose I did (1) you -- what would that mean?
    reply: Does someone else believe I (1) you?
  decomp: (.*)you (.*)
    reply: We were discussing you -- not me.
    reply: Oh, I (1)?
    reply: You're not really talking about me -- are you?
    reply: What are your feelings now?
key: am 0
  decomp: (.*)am i (.*)
    reply: Do you believe you are (1)?
    reply: Would you want to be (1)?
    reply: Do you wish I would tell you you are (1)?
    reply: What would it mean if you were (1)?
    reply: goto what
  decomp: (.*)i am (.*)
	reply: goto i
key: are 0
  decomp: (.*)are you (.*)
    reply: Why are you interested in whether I am (1) or not?
    reply: Would you prefer if I weren't (1)?
    reply: Perhaps I am (1) in your fantasies.
    reply: Do you sometimes think I am (1)?
    reply: goto what
	reply: Would it matter to you?
	reply: What if I were (1)?
  decomp: (.*)you are (.*)
	reply: goto you
  decomp: (.*)are (.*)
    reply: Did you think they might not be (1)?
    reply: Would you like it if they were not (1)?
    reply: What if they were not (1)?
	reply: Are they always (1)?
    reply: Possibly they are (1).
	reply: Are you positive they are (1)?
key: your 0
  decomp: (.*)your (.*)
    reply: Why are you concerned over my (1)?
    reply: What about your own (1)?
    reply: Are you worried about someone else's (1)?
    reply: Really, my (1)?
	reply: What makes you think of my (1)?
	reply: Do you want my (1)?

# wh* h*
key: what 0
  decomp: (.*)
    reply: Why do you ask?
    reply: Does that question interest you?
    reply: What is it you really want to know?
    reply: Are such questions much on your mind?
    reply: What answer would please you most?
    reply: What do you think?
    reply: What comes to mind when you ask that?
    reply: Have you asked such questions before?
    reply: Have you asked anyone else?
key: who 0
   decomp: who (.*)
	reply: goto what
key: when 0
   decomp: when (.*)
	reply: goto what
key: where 0
   decomp: where (.*)
	reply: goto what
key: how 0
   decomp: how (.*)
	reply: goto what
key: why 0
  decomp: (.*)why don't you (.*)
    reply: Do you believe I don't (1)?
    reply: Perhaps I will (1) in good time.
    reply: Should you (1) yourself?
    reply: You want me to (1)?
    reply: goto what
  decomp: (.*)why can't i (.*)
    reply: Do you think you should be able to (1)?
    reply: Do you want to be able to (1)?
    reply: Do you believe this will help you to (1)?
    reply: Have you any idea why you can't (1)?
    reply: goto what
  decomp: (.*)
    reply: goto what

# explain
key: because 0
  decomp: (.*)
    reply: Is that the real reason?
    reply: Don't any other reasons come to mind?
    reply: Does that reason seem to explain anything else?
    reply: What other reasons might there be?

# different
key: different 0
   decomp: (.*)
	reply: How is it different?
	reply: What differences do you see?
	reply: What does that difference suggest to you?
	reply: What other distinctions do you see?
	reply: What do you suppose that disparity means?
	reply: Could there be some connection, do you suppose?
	reply: How?

# foreign languages
key: deutsch 0
  decomp: (.*)
    reply: goto xforeign
    reply: I told you before, I don't understand German.
key: francais 0
  decomp: (.*)
    reply: goto xforeign
    reply: I told you before, I don't understand French.
key: italiano 0
  decomp: (.*)
    reply: goto xforeign
    reply: I told you before, I don't understand Italian.
key: espanol 0
  decomp: (.*)
    reply: goto xforeign
    reply: I told you before, I don't understand Spanish.
key: xforeign 0
  decomp: (.*)
    reply: I speak only English.

# ===================== rank 1 ==========================
key: always 1
  decomp: (.*)
    reply: Can you think of a specific example?
    reply: When?
    reply: What incident are you thinking of?
    reply: Really, always?

# ===================== rank 2 ==========================
key: was 2
  decomp: (.*)was i (.*)
    reply: What if you were (1)?
    reply: Do you think you were (1)?
    reply: Were you (1)?
    reply: What would it mean if you were (1)?
    reply: What does '(1)' suggest to you?
    reply: goto what
  decomp: (.*)i was (.*)
    reply: Were you really?
    reply: Why do you tell me you were (1) now?
    reply: Perhaps I already know you were (1).
  decomp: (.*)was you (.*)
    reply: Would you like to believe I was (1)?
    reply: What suggests that I was (1)?
    reply: What do you think?
    reply: Perhaps I was (1).
    reply: What if I had been (1)?
key: my 2
  decomp: $ (.*)my (.*)
    reply: Lets discuss further why your (1).
    reply: Earlier you said your (1).
    reply: But your (1).
    reply: Does that have anything to do with the fact that your (1)?
  decomp: (.*)my(.*)@family (.*)
    reply: Tell me more about your family.
    reply: Who else in your family (4)?
    reply: Your @0?
    reply: What else comes to mind when you think of your @0?
  decomp: (.*)my (.*)
    reply: Your (1)?
    reply: Why do you say your (1)?
    reply: Does that suggest anything else which belongs to you?
    reply: Is it important to you that your (1)?
key: everyone 2
  decomp: (.*)@everyone (.*)
    reply: Really, (1)?
    reply: Surely not (1).
    reply: Can you think of anyone in particular?
    reply: Who, for example?
    reply: Are you thinking of a very special person?
    reply: Who, may I ask?
    reply: Someone special perhaps?
    reply: You have a particular person in mind, don't you?
    reply: Who do you think you're talking about?
key: everybody 2
  decomp: (.*)
    reply: goto everyone
key: nobody 2
  decomp: (.*)
    reply: goto everyone
key: noone 2
  decomp: (.*)
    reply: goto everyone

# ===================== rank 3 ==========================
key: if 3
  decomp: (.*)if (.*)
    reply: Do you think its likely that (1)?
    reply: Do you wish that (1)?
    reply: What do you know about (1)?
    reply: Really, if (1)?
	reply: What would you do if (1)?
	reply: But what are the chances that (1)?
	reply: What does this speculation lead to?
key: dreamed 3
  decomp: (.*)i dreamed (.*)
    reply: Really, (1)?
    reply: Have you ever fantasized (1) while you were awake?
    reply: Have you ever dreamed (1) before?
    reply: goto dream
key: dream 3
  decomp: (.*)
    reply: What does that dream suggest to you?
    reply: Do you dream often?
    reply: What persons appear in your dreams?
    reply: Do you believe that dreams have something to do with your problem?

# ===================== rank 4 ==========================
key: remember 4
  decomp: (.*)i remember (.*)
    reply: Do you often think of (1)?
    reply: Does thinking of (1) bring anything else to mind?
    reply: What else do you recollect?
    reply: Why do you remember (1) just now?
    reply: What in the present situation reminds you of (1)?
    reply: What is the connection between me and (1)?
	reply: What else does (1) remind you of?
  decomp: (.*)do you remember (.*)
    reply: Did you think I would forget (1)?
    reply: Why do you think I should recall (1) now?
    reply: What about (1)?
    reply: goto what
    reply: You mentioned (1)?
   decomp: (.*)you remember (.*)
	reply: How could I forget (1)?
	reply: What about (1) should I remember?
	reply: goto you
key: forget 4
   decomp: (.*)i forget (.*)
	reply: Can you think of why you might forget (1)?
	reply: Why can't you remember (1)?
	reply: How often do you think of (1)?
	reply: Does it bother you to forget that?
	reply: Could it be a mental block?
	reply: Are you generally forgetful?
	reply: Do you think you are suppressing (1)?
   decomp: (.*)did you forget (.*)
	reply: Why do you ask?
	reply: Are you sure you told me?
	reply: Would it bother you if I forgot (1)?
	reply: Why should I recall (1) just now?
	reply: goto what
	reply: Tell me more about (1).

# ===================== rank 5 ==========================
key: alike 5
  decomp: (.*)
    reply: In what way?
    reply: What resemblence do you see?
    reply: What does that similarity suggest to you?
    reply: What other connections do you see?
    reply: What do you suppose that resemblence means?
    reply: What is the connection, do you suppose?
    reply: Could there really be some connection?
    reply: How?
key: like 5
  decomp: (.*)@be (.*)like (.*)
    reply: goto alike

# ===================== rank 10 ==========================
key: name 10
  decomp: (.*)
    reply: I am not interested in names.
    reply: I've told you before, I don't care about names -- please continue.

# ===================== rank 50 ==========================
key: computer 50
  decomp: (.*)
    reply: Do computers worry you?
    reply: Why do you mention computers?
    reply: What do you think machines have to do with your problem?
    reply: Don't you think computers can help people?
    reply: What about machines worries you?
    reply: What do you think about machines?
	reply: You don't think I am a computer program, do you?




