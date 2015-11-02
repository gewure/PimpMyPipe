#!/bin/perl

# AliceInWonderland-Liste Filtern nach Dictionary und 'nutzlose' Wörter rausstreichen;
my @dictionary = "";#qw(and is was be \. \, \! \?); # dictionary array of strings

while(<>) {
	my @ListOfLines; #Lines-Array of Strings
	push  @ListOfLines, $_; #add the current line to the Array

	#foreach line @ListOfLines:
	for $line (@ListOfLines) {
		#clean the $line from words in @dictionary
		my $combined_search = join("|",@dictionary);
		($line =~ s/($combined_search)//);

		my @ListOfWords = split ' ',$line; #split the line into words
		#print("##########This Sentence contains " . $#ListOfWords ." words \n"); #count words

		print "@ListOfWords\n"; #original word
		#print "#### 1. MIX #### @ListOfWords[-1, 1..$#ListOfWords-1, 0]$/";

		my $counter = $#ListOfWords;
		my $pointer = 1;
		for ($counter > 0) {
			my @NewListOfWords = @ListOfWords[-$pointer, 1.. $#ListOfWords-1, 0];
			#print "@NewListOfWords[-1, 1..$#NewListOfWords-1, 0]\n";
			print "@NewListOfWords\n";
			$counter--;
			$pointer++;
		}


	}
}
